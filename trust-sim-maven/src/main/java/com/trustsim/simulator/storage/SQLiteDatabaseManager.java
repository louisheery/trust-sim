package com.trustsim.simulator.storage;

import com.trustsim.synthesiser.AgentSystem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseManager {

  private static SQLiteDatabaseManager INSTANCE;
  private static final String DB_PATH = "jdbc:sqlite:";
  private static final String DB_NAME = "src/trustsimdb.sqlite";
//  private final XStreamManager xStreamManager = new XStreamManager();
  private static Connection connDB = null;

  private SQLiteDatabaseManager() {
  }

  public static Connection getConn() throws Exception {
    if(connDB == null){
      Class.forName("org.sqlite.JDBC");
      connDB = DriverManager.getConnection(DB_PATH + DB_NAME);
      connDB.setAutoCommit(false);
    }
    return connDB;
  }

  public static SQLiteDatabaseManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SQLiteDatabaseManager();
    }
    return INSTANCE;
  }

  public void addTable(String tableName) throws Exception {

    try {
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED1");
      System.out.println("11d1");
      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "";
      if (tableName.equals("agentSystems")) {
         sqlQuery = "CREATE TABLE IF NOT EXISTS agentSystems (ITEMNAME TEXT PRIMARY KEY NOT NULL, NUMCONSUMERS INTEGER NOT NULL, NUMPRODUCERS INTEGER NOT NULL, NUMSERREQ INTEGER NOT NULL)";
      } else if (tableName.equals("trustModels")) {
         sqlQuery = "CREATE TABLE IF NOT EXISTS trustModels (ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMXML TEXT NOT NULL)";
      }

      System.out.println(sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ADJKAS" + e.toString());
      System.out.println(e.getMessage());
    }
  }

  public boolean addToTable(String tableName, String itemName, List<Integer> itemData) {

    try {
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");

      Statement sqlExecutor = dbConnection.createStatement();

      if (!findInTable(tableName, itemName)) {
        String sqlQuery = "";
        if (tableName.equals("agentSystems")) {
          // (ITEMNAME TEXT PRIMARY KEY NOT NULL, NUMCONSUMERS INTEGER NOT NULL, NUMPRODUCERS INTEGER NOT NULL, NUMSERREQ INTEGER NOT NULL)
          sqlQuery = "INSERT INTO agentSystems (ITEMNAME, NUMCONSUMERS, NUMPRODUCERS, NUMSERREQ) VALUES ('" + itemName + "', " + itemData.get(0) + ", " + itemData.get(1) + ", " + itemData.get(2) + ");";
        } else {
          sqlQuery = "INSERT INTO trustModels (ITEMNAME, ITEMXML) VALUES ('" + itemName + "', '" + itemData.get(0) + "');";
        }
        System.out.println(sqlQuery);
        sqlExecutor.executeUpdate(sqlQuery);
        sqlExecutor.close();
        dbConnection.commit();

        return true;
      }

    } catch (Exception e) {
      System.out.println("EEEE" + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }

  private boolean findInTable(String tableName, String itemName) throws SQLException {

    Connection dbConnection = null;
    try {
      dbConnection = getConn();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Statement sqlExecutor = null;

    try {
      System.out.println("DATABASE OPENED");

      sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "";

      if (tableName.equals("agentSystems")) {
        sqlQuery = "SELECT COUNT(*) FROM 'agentSystems' WHERE ITEMNAME = '" + itemName + "'";
      } else {
        sqlQuery = "SELECT COUNT(*) FROM trustModels WHERE ITEMNAME = '" + itemName + "'";
      }


      try (ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery)) {
        // Only expecting a single result
        System.out.println("BB" + resultSet.toString());
        if (resultSet.next()) {
          System.out.println("BB1" + resultSet.toString());
          boolean containsRow = resultSet.getBoolean(1); // "found" column
          System.out.println("BB" + containsRow);
          if (containsRow) {
            System.out.println("true191");
            sqlExecutor.close();
            return true;
          } else {
            System.out.println("false191");
            sqlExecutor.close();
            return false;
          }
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
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");

      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "DELETE FROM " + tableName + " WHERE ITEMNAME = '" + agentSystemName + "'";
      System.out.println("AA" + sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private List<String> getFromTable(String tableName, String itemName) {

    List<String> itemData = new ArrayList<>();
    //itemData.add(itemName);

    try {
      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");

      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT * FROM " + tableName + " WHERE ITEMNAME = " + itemName;
      ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();

      while (resultSet.next()) {
        for (int i = 1; i <= 4; i++) {
          itemData.add(resultSet.getString(i));
        }
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return itemData;

  }

  private List<List<String>> getAllColumnDataFromTable(String tableName) {

    List<List<String>> itemsData = new ArrayList<>();

    try {

      Connection dbConnection = getConn();
      System.out.println("DATABASE OPENED");


      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT * FROM " + tableName;
      ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery);

      int row = 0;
      while (resultSet.next()) {
        itemsData.add(new ArrayList<>());
        for (int i = 1; i <= 4; i++) {
          itemsData.get(row).add(resultSet.getString(i));
        }
        row++;
      }

      sqlExecutor.close();
      dbConnection.commit();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return itemsData;
  }

  public ObservableList<AgentSystem> retrieveSystemTableData(String tableName) {

    List<List<String>> itemsData = getAllColumnDataFromTable(tableName);

    List<AgentSystem> agentSystemList = new ArrayList<>();
    System.out.println("**" + itemsData);
    for (List<String> itemData : itemsData) {
      AgentSystem agentSystem = new AgentSystem(itemData.get(0), itemData.get(1), itemData.get(2), itemData.get(3));
      agentSystemList.add(agentSystem);
    }

    //AgentSystem agentSystem = new AgentSystem("TEST", "1", "2", "3");
    //agentSystemList.add(agentSystem);
    return FXCollections.observableList(agentSystemList);
  }

//  public ObservableList<TrustModel> retrieveModelTableData(String tableName) {
//
//    List<List<String>> itemsData = getAllColumnDataFromTable(tableName);
//
//    List<TrustModel> trustModelList = new ArrayList<>();
//
//    for (List<String> itemData : itemsData) {
//      TrustModel trustModel = new WangTrustModel();
//      trustModelList.add(trustModel);
//    }
//
//    return FXCollections.observableList(trustModelList);
//  }
}