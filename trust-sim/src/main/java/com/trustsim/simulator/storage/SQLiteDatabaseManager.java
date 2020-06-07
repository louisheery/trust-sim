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
  private final XStreamManager xStreamManager = new XStreamManager();

  private SQLiteDatabaseManager() {
  }

  public static SQLiteDatabaseManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SQLiteDatabaseManager();
    }
    return INSTANCE;
  }


  public void openDatabase() {

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);


    } catch (SQLException | ClassNotFoundException e) {
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("DATABASE OPENED");
  }

  public void addTable(String tableName) {

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED1");
      System.out.println("11d1");
      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "CREATE TABLE IF NOT EXISTS " + tableName + "(ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMXML TEXT NOT NULL)";
      System.out.println(sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();
      dbConnection.close();

    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("ADJKAS" + e.toString());
      System.out.println(e.getMessage());
    }
  }

  public boolean addToTable(String tableName, String itemName, String itemDataXML) {

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED");

      dbConnection.setAutoCommit(false);

      Statement sqlExecutor = dbConnection.createStatement();

      if (!findInTable(tableName, itemName)) {
        String sqlQuery = "INSERT INTO " + tableName + "(ITEMNAME, ITEMXML) VALUES ('" + itemName + "', '" + itemDataXML + "');";
        System.out.println(sqlQuery);
        sqlExecutor.executeUpdate(sqlQuery);
        sqlExecutor.close();
        dbConnection.commit();
        dbConnection.close();

        return true;
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return false;
  }

  private boolean findInTable(String tableName, String itemName) throws SQLException {

    Connection dbConnection = null;
    Statement sqlExecutor = null;

    try {
      Class.forName("org.sqlite.JDBC");
      dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED");

      dbConnection.setAutoCommit(false);

      sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT COUNT(*) FROM " + tableName + " WHERE ITEMNAME = '" + itemName + "'";
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
            dbConnection.close();
            return true;
          } else {
            System.out.println("false191");
            sqlExecutor.executeUpdate(sqlQuery);
            sqlExecutor.close();
            dbConnection.commit();
            dbConnection.close();
            return false;
          }
        }
      }


      return sqlQuery.equals("0");

    } catch (Exception e) {
      System.out.println("@^" + e.getMessage() + "@^");
    }
    sqlExecutor.close();
    dbConnection.commit();
    dbConnection.close();
    return false;
  }

  public void removeFromTable(String tableName, String agentSystemName) {
    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED");
      dbConnection.setAutoCommit(false);

      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "DELETE FROM " + tableName + " WHERE ITEMNAME = '" + agentSystemName + "'";
      System.out.println("AA" + sqlQuery);
      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();
      dbConnection.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private String getFromTable(String tableName, String itemId) {

    String agentSystemXML = null;

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED");

      dbConnection.setAutoCommit(false);

      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT * FROM " + tableName + " WHERE ITEMNAME = " + itemId;
      ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery);
      sqlExecutor.close();
      dbConnection.commit();
      dbConnection.close();

      while (resultSet.next()) {
        agentSystemXML = resultSet.getString("ITEMXML");
      }

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return agentSystemXML;

  }

  private List<String> getAllColumnDataFromTable(String tableName, String columnName) {

    List<String> agentSystemsXML = new ArrayList<>();

    try {
      Class.forName("org.sqlite.JDBC");
      Connection dbConnection = DriverManager.getConnection(DB_PATH + DB_NAME);
      System.out.println("DATABASE OPENED");

      dbConnection.setAutoCommit(false);

      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = "SELECT * FROM " + tableName;
      ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery);

      while (resultSet.next()) {
        String s = resultSet.getString(columnName);
        agentSystemsXML.add(s);
      }

      sqlExecutor.close();
      dbConnection.commit();
      dbConnection.close();

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return agentSystemsXML;
  }

  public ObservableList<AgentSystem> retrieveSystems(String tableName) {

    List<String> XMLStrings = getAllColumnDataFromTable(tableName, "ITEMXML");

    List<AgentSystem> agentSystemList = new ArrayList<>();

    for (String xmlString : XMLStrings) {
      AgentSystem agentSystem = xStreamManager.decodeFromXML(xmlString);
      agentSystemList.add(agentSystem);
    }

    return FXCollections.observableList(agentSystemList);
  }
}
