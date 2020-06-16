package com.trustsim.simulator.storage;

import com.trustsim.simulator.trustmodel.TrustModel;
import com.trustsim.synthesiser.AgentSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseManager {

  private static SQLiteDatabaseManager INSTANCE;
  private static final String DB_PATH = "jdbc:sqlite:";
  private static final String DB_NAME = "src/trustsimdb.sqlite";
  private static Connection dbConnection = null;
  //private final XStreamManager xStreamManager = new XStreamManager();

  private SQLiteDatabaseManager() {

  }

  public static Connection getConn() throws Exception {
    if(dbConnection == null){
      Class.forName("org.sqlite.JDBC");
      try {
        dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
        dbConnection.setAutoCommit(false);
      } catch (SQLException e) {
        System.out.println("EXCEPTIONNN: " + e.toString());
        return null;
      }
    }
    return dbConnection;
}

  public static SQLiteDatabaseManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SQLiteDatabaseManager();
    }
    return INSTANCE;
  }

  public void addTable(String tableName) {

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED1");
      System.out.println("11d1");
      Statement sqlExecutor = dbConnection.createStatement();

   String sqlQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(ITEMNAME INTEGER PRIMARY KEY AUTOINCREMENT, OBJECTNAME TEXT NOT NULL, OBJECTCODE BLOB)";
    // String sq = "CREATE TABLE IF NOT EXISTS " + tableName + "(ITEMNAME TEXT PRIMARY KEY NOT NULL, OBJECTNAME TEXT NOT NULL, OBJ";
//
//    (ITEMNA) int(11) NOT NULL auto_increment,\n" +
//    "`obj_name` varchar(20) default NULL,\n" +
//    "`obj` blob,\n" +
//    "PRIMARY KEY (`id`)\n" +
//    ")";
      System.out.println(sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();
      

    } catch (Exception e) {
      System.out.println("ADJKAS" + e.toString());
      System.out.println(e.getMessage());
    }
  }

  public boolean addToTable(String tableName, String itemName, Object objToStore) {

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");



      String SQL_QUERY;
      if (tableName.equals("agentSystems")) {
        SQL_QUERY = "INSERT INTO agentSystems(OBJECTNAME, OBJECTCODE) VALUES (?, ?)";
      } else {
        SQL_QUERY = "INSERT INTO trustModels(OBJECTNAME, OBJECTCODE) VALUES (?, ?)";
      }
      PreparedStatement sqlExecutor = dbConnection.prepareStatement(SQL_QUERY);

      if (!findInTable(tableName, itemName)) {
//        String sqlQuery = "INSERT INTO " + tableName + "(ITEMNAME, OBJECTNAME, OBJECTCODE) VALUES ('" + itemName + "', '" + itemName + "', '"  + itemDataXML + "');";
//        System.out.println(sqlQuery);
        System.out.println("R -- " + SQL_QUERY);
        sqlExecutor.setString(1, itemName);
        System.out.print("AADS--" + sqlExecutor.toString());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(objToStore);
        System.out.println("A");
        oos.flush();
        System.out.println("A");
        oos.close();
        System.out.println("A");
        byte[] data = bos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        System.out.println("A");
        bos.close();
        System.out.println("A");
        sqlExecutor.setBytes(2, data);
        sqlExecutor.executeQuery();
        System.out.println("A");
        ResultSet resultSet = sqlExecutor.getGeneratedKeys();
        System.out.println("A");
        int objectID = -1;
        if (resultSet.next()) {
          objectID = resultSet.getInt(1);
        }
        System.out.println("object" + objectID);
        sqlExecutor.close();
        dbConnection.commit();
        
        System.out.println("Object added to DB: " + objToStore.toString() + " with ID: " + objectID);
        return true;
      }

    } catch (Exception e) {
      System.out.println("ERRRROR: " + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  private boolean findInTable(String tableName, String itemName) throws SQLException {

    Connection dbConnection = null;
    Statement sqlExecutor = null;

    try {
      Class.forName("org.sqlite.JDBC");
      dbConnection = getConn();
      System.out.println("DATABASE OPENED");


      sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE OBJECTNAME = '" + itemName + "'";
      System.out.println("%%" + sqlQuery);


      try (ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery)) {
        // Only expecting a single result
        if (resultSet.next()) {
          boolean containsRow = resultSet.getBoolean(1); // "found" column
          if (containsRow) {
            System.out.println("true191");
            sqlExecutor.executeUpdate(sqlQuery);
            sqlExecutor.close();
            dbConnection.commit();

            return true;
          } else {
            System.out.println("false191");
            sqlExecutor.executeUpdate(sqlQuery);
            sqlExecutor.close();
            dbConnection.commit();

            return false;
          }
        }


      } catch (Exception e) {
        System.out.println("dddd@^" + e.getMessage() + "@^");
        if (sqlExecutor != null) {
          sqlExecutor.close();
          dbConnection.commit();

        }
      }

    } catch (Exception e) {
      System.out.println("@^" + e.getMessage() + "@^");
      if (sqlExecutor != null) {
        sqlExecutor.close();
        dbConnection.commit();

      }
    }

    return false;
  }

  public void removeFromTable(String tableName, String agentSystemName) {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection =  getConn();
      System.out.println("DATABASE OPENED");


      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "DELETE FROM " + tableName + " WHERE OBJECTNAME = '" + agentSystemName + "'";
      System.out.println("AA" + sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();
      
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private String getFromTable(String tableName, String itemId) {

    String agentSystemXML = null;

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");



      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT * FROM " + tableName + " WHERE OBJECTNAME = " + itemId;
      ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();
      

      while (resultSet.next()) {
        agentSystemXML = resultSet.getString("OBJECTCODE");
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return agentSystemXML;

  }

  private List<Object> getAllColumnDataFromTable(String tableName, String columnName) {

    List<Object> agentSystemsXML = new ArrayList<>();

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = getConn();
      System.out.println("1DATABASE OPENED");



      String SQL_STATEMENT;
      if (tableName.equals("agentSystems")) {
        SQL_STATEMENT = "SELECT OBJECTCODE FROM agentSystems WHERE ITEMNAME = 3";
      } else {
        SQL_STATEMENT = "SELECT OBJECTCODE FROM trustModels WHERE ITEMNAME = 3";
      }
      PreparedStatement sqlExecutor = dbConnection.prepareStatement(SQL_STATEMENT);
      System.out.println("A0");
      ResultSet resultSet = sqlExecutor.executeQuery();
      System.out.println("A1");
      byte[] buf = resultSet.getBytes(1);
      System.out.println("A2");
      ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf));;
      System.out.println("A4");
      System.out.println("A5");
      List<Object> input = (List<Object>) objectInputStream.readObject();
      System.out.println("A6");
      for (Object l : input) {
        agentSystemsXML.add(input);
      }
      System.out.println("A7");
      System.out.println("A7");
//      if (tableName.equals("agentSystems")) {
//
////        AgentSystem deSerializedObject = (AgentSystem) objectInputStream.readObject();
////        System.out.println("SYSTEMFROMDB: " + deSerializedObject.toString() + deSerializedObject);
////        agentSystemsXML.add(deSerializedObject);
//      } else {
//
////        TrustModel deSerializedObject = (TrustModel) objectInputStream.readObject();
////        agentSystemsXML.add(deSerializedObject);
//      }



      sqlExecutor.close();
      dbConnection.commit();
      

    } catch (Exception e) {
      System.out.println("XA!");
      System.out.println(e.getMessage());
    }

    return agentSystemsXML;
  }

  public ObservableList<AgentSystem> retrieveSystemTableData(String tableName) {

    List<Object> XMLStrings = getAllColumnDataFromTable(tableName, "OBJECTCODE");

    List<AgentSystem> agentSystemList = new ArrayList<>();

    for (Object xmlString : XMLStrings) {
//      AgentSystem agentSystem = (AgentSystem) xStreamManager.decodeFromXML(xmlString);
      agentSystemList.add((AgentSystem) xmlString);
    }

    return FXCollections.observableList(agentSystemList);
  }

  public ObservableList<TrustModel> retrieveModelTableData(String tableName) {

    List<Object> XMLStrings = getAllColumnDataFromTable(tableName, "OBJECTCODE");

    List<TrustModel> trustModelList = new ArrayList<>();

    for (Object xmlString : XMLStrings) {
      //TrustModel trustModel = (TrustModel) xStreamManager.decodeFromXML(xmlString);
      trustModelList.add((TrustModel) xmlString);
    }

    return FXCollections.observableList(trustModelList);
  }
}
