package com.trustsim.storage;

import com.google.gson.Gson;
import com.trustsim.simulator.trustmodel.DynamicTrustModel;
import com.trustsim.simulator.trustmodel.EigenTrustModel;
import com.trustsim.synthesiser.AgentSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Singleton Class used to interface with one or more SQLite Database tables.
 */
public class SQLiteDatabaseManager {

  private static SQLiteDatabaseManager INSTANCE;
  private static final String DB_PATH = "jdbc:sqlite:";
//  private static final String DB_NAME = ":memory:";
private static final String DB_NAME = "trustsimdb.sqlite";
//  private static final String DB_NAME = "src/trustsimdb.sqlite";
  private static Connection connDB = null;
  private final Gson gson = new Gson();

  private SQLiteDatabaseManager() {
  }

  /**
   * Static function used to connect to the Trust Sim SQLite database.
   *
   * @return Connection object to the database
   * @throws Exception if connection to database fails
   */
  public static Connection getConn() throws Exception {
    if (connDB == null) {
      Class.forName("org.sqlite.JDBC");
      connDB = DriverManager.getConnection(DB_PATH + DB_NAME);
      connDB.setAutoCommit(false);
    }
    return connDB;
  }

  /**
   * Singleton instance getter function.
   * @return Reference to Singleton instance of this class
   */
  public static SQLiteDatabaseManager getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new SQLiteDatabaseManager();
    }
    return INSTANCE;
  }

  /**
   * Function used to add a new table to the currently open SQLite database.
   * @param tableName name of table to be added
   * @throws Exception thrown if table is unable to be added
   */
  public void addTable(String tableName) throws Exception {

    try {
      Connection dbConnection = getConn();
      Statement sqlExecutor = dbConnection.createStatement();

      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "CREATE TABLE IF NOT EXISTS agentSystems (ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMCLASS TEXT NOT NULL, ITEMXML TEXT NOT NULL)";
        case "agentSystemsCalculator" -> "CREATE TABLE IF NOT EXISTS agentSystemsCalculator (ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMCLASS TEXT NOT NULL, ITEMXML TEXT NOT NULL)";
        case "trustModels" -> "CREATE TABLE IF NOT EXISTS trustModels (ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMXML TEXT NOT NULL)";
        case "simulationResults" -> "CREATE TABLE IF NOT EXISTS simulationResults (ITEMNAME TEXT PRIMARY KEY NOT NULL, ITEMCLASS TEXT NOT NULL, ITEMXML TEXT NOT NULL)";
        default -> "";
      };

      sqlExecutor.executeUpdate(sqlQuery);
      sqlExecutor.close();

    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Function used to determine whether an item with a particular name is present in a table
   * with a particular name in the currently open SQLite database.
   * @param tableName name of table to be searched in
   * @param itemName name of item to be searched for
   * @return whether item is found or not
   * @throws SQLException thrown if table is unable to be searched
   */
  public boolean findInTable(String tableName, String itemName) throws SQLException {

    Connection dbConnection = null;
    try {
      dbConnection = getConn();
    } catch (Exception e) {
      e.printStackTrace();
    }
    Statement sqlExecutor = null;

    try {
      assert dbConnection != null;
      sqlExecutor = dbConnection.createStatement();

      String sqlQuery;
      if (tableName.equals("agentSystems")) {
        sqlQuery = "SELECT COUNT(*) FROM 'agentSystems' WHERE ITEMNAME = '" + itemName + "'";
      } else if (tableName.equals("agentSystemsCalculator")) {
        sqlQuery = "SELECT COUNT(*) FROM 'agentSystemsCalculator' WHERE ITEMNAME = '" + itemName + "'";
      } else if (tableName.equals("trustModels")) {
        sqlQuery = "SELECT COUNT(*) FROM 'trustModels' WHERE ITEMNAME = '" + itemName + "'";
      } else {
        sqlQuery = "SELECT COUNT(*) FROM 'simulationResults' WHERE ITEMNAME = '" + itemName + "'";
      }

      try (ResultSet resultSet = sqlExecutor.executeQuery(sqlQuery)) {
        // Only expecting a single result
        if (resultSet.next()) {
          boolean containsRow = resultSet.getBoolean(1);
          sqlExecutor.close();
          return containsRow;
        }
      }

    } catch (Exception e) {
      if (sqlExecutor != null) {
        sqlExecutor.close();
        dbConnection.commit();
      }
    }

    return false;
  }

  /**
   * Function used to add a new object with a particular name to a particular table in the
   * currently open SQLite database.
   * @param tableName name of table to add object to
   * @param objectName name of object in the table to be added
   * @param object object item to be added to table
   * @return whether or not the object is successfully added to the table
   */
  public boolean addObjectToTable(String tableName, String objectName, Object object) {

    try {
      Connection dbConnection = getConn();
      PreparedStatement sqlStatement = null;

      if (!findInTable(tableName, objectName)) {

        String objectClassName = object.getClass().toString();

        String sql;
        switch (tableName) {
          case "agentSystems" -> {
            sql = "INSERT INTO agentSystems (ITEMNAME, ITEMCLASS, ITEMXML) VALUES (?, ?, ?)";
            sqlStatement = dbConnection.prepareStatement(sql);
            sqlStatement.setString(1, objectName);
            sqlStatement.setString(2, objectClassName);
            switch (objectClassName) {
              case "class com.trustsim.synthesiser.AgentSystem" -> {
                sqlStatement.setString(3, gson.toJson(object, AgentSystem.class));
              }
            }
          }
          case "agentSystemsCalculator" -> {
            sql = "INSERT INTO agentSystemsCalculator (ITEMNAME, ITEMCLASS, ITEMXML) VALUES (?, ?, ?)";
            sqlStatement = dbConnection.prepareStatement(sql);
            sqlStatement.setString(1, objectName);
            sqlStatement.setString(2, objectClassName);
            switch (objectClassName) {
              case "class com.trustsim.synthesiser.AgentSystem" -> {
                sqlStatement.setString(3, gson.toJson(object, AgentSystem.class));
              }
            }

          }
          case "trustModels" -> {
            sql = "INSERT INTO trustModels (ITEMNAME, ITEMXML) VALUES (?, ?)";
            sqlStatement = dbConnection.prepareStatement(sql);
            sqlStatement.setString(1, objectName);

            switch (objectClassName) {
              case "class com.trustsim.simulator.trustmodel.EigenTrustModel" -> {
                sqlStatement.setString(2, gson.toJson(object, EigenTrustModel.class));
              }

              case "class com.trustsim.simulator.trustmodel.DynamicTrustModel" -> {
                sqlStatement.setString(2, gson.toJson(object, DynamicTrustModel.class));
              }
            }
          }
          case "simulationResults" -> {
            sql = "INSERT INTO simulationResults (ITEMNAME, ITEMCLASS, ITEMXML) VALUES (?, ?, ?)";
            sqlStatement = dbConnection.prepareStatement(sql);
            sqlStatement.setString(1, objectName);
            sqlStatement.setString(2, objectClassName);

            switch (objectClassName) {
              case "class com.trustsim.synthesiser.AgentSystem" -> {
                sqlStatement.setString(3, gson.toJson(object, AgentSystem.class));
              }
            }
          }
        }
        if (sqlStatement != null) {
          sqlStatement.execute();
        }
        dbConnection.commit();

        return true;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Function used to remove a new object with a particular name to a particular table in the
   * currently open SQLite database.
   * @param tableName name of table to remove object to
   * @param objectName name of object in the table to be removed
   */
  public void removeObjectFromTable(String tableName, String objectName) {

    try {
      Connection dbConnection = getConn();
      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "DELETE FROM agentSystems WHERE ITEMNAME = ?";
        case "agentSystemsCalculator" -> "DELETE FROM agentSystemsCalculator WHERE ITEMNAME = ?";
        case "trustModels" -> "DELETE FROM trustModels WHERE ITEMNAME = ?";
        case "simulationResults" -> "DELETE FROM simulationResults WHERE ITEMNAME = ?";
        default -> "";
      };
      PreparedStatement sqlStatement = dbConnection.prepareStatement(sqlQuery);
      sqlStatement.setString(1, objectName);
      sqlStatement.execute();
      dbConnection.commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Function used to remove all objects from a particular table in the currently open SQLite
   * database.
   * @param tableName name of table to remove object to
   */
  public void removeAllObjectsFromTable(String tableName) {

    try {
      Connection dbConnection = getConn();
      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "DELETE FROM agentSystems";
        case "agentSystemsCalculator" -> "DELETE FROM agentSystemsCalculator";
        case "trustModels" -> "DELETE FROM trustModels";
        case "simulationResults" -> "DELETE FROM simulationResults";
        default -> "";
      };
      PreparedStatement sqlStatement = dbConnection.prepareStatement(sqlQuery);
      sqlStatement.execute();
      dbConnection.commit();
      sqlStatement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Function used to retrieve all Agent System objects that exist within a particular table in the
   * currently open SQLite database.
   * @param tableName name of table to retrieve all objects from
   * @return observable list of objects obtained from table
   */
  public ObservableList<AgentSystem> retrieveAllSystemObjectsTableData(String tableName) {

    List<AgentSystem> itemData = new ArrayList<>();

    try {

      Connection dbConnection = getConn();

      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "SELECT * FROM agentSystems";
        case "agentSystemsCalculator" -> "SELECT * FROM agentSystemsCalculator";
        case "trustModels" -> "SELECT * FROM trustModels";
        case "simulationResults" -> "SELECT * FROM simulationResults";
        default -> "";
      };

      PreparedStatement sqlStatement = dbConnection.prepareStatement(sqlQuery);
      ResultSet resultSet = sqlStatement.executeQuery();

      while (resultSet.next()) {
        switch (resultSet.getString(2)) {
          case "class com.trustsim.synthesiser.AgentSystem" -> {
            itemData.add(gson.fromJson(resultSet.getString(3), AgentSystem.class));
          }
        }
      }

      dbConnection.commit();
      sqlStatement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return FXCollections.observableList(itemData);
  }

  /**
   * Function used to retrieve all objects that exist within a particular table in the currently
   * open SQLite database.
   * @param tableName name of table to retrieve all objects from
   * @return observable list of objects obtained from table
   */
  public List<Object> retrieveAllObjectsTableData(String tableName) {

    List<Object> output = new ArrayList<>();

    try {

      Connection dbConnection = getConn();

      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "SELECT * FROM agentSystems";
        case "agentSystemsCalculator" -> "SELECT * FROM agentSystemsCalculator";
        case "trustModels" -> "SELECT * FROM trustModels";
        case "simulationResults" -> "SELECT * FROM simulationResults";
        default -> "";
      };
      PreparedStatement sqlStatement = dbConnection.prepareStatement(sqlQuery);
      ResultSet resultSet = sqlStatement.executeQuery();

      while (resultSet.next()) {
        output.add(resultSet.getString(3));
      }

      dbConnection.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return output;
  }

  /**
   * Function used to retrieve all objects that exist within a particular table in the currently
   * open SQLite database
   * @param tableName name of table to retrieve object from
   * @param objectName name of object to be retrieved from table
   * @return object obtained from table
   */
  public Object retrieveObjectTableData(String tableName, String objectName) {

    Object output = null;

    try {

      Connection dbConnection = getConn();

      String sqlQuery = switch (tableName) {
        case "agentSystems" -> "SELECT * FROM agentSystems WHERE ITEMNAME = ?";
        case "agentSystemsCalculator" -> "SELECT * FROM agentSystemsCalculator WHERE ITEMNAME = ?";
        case "trustModels" -> "SELECT * FROM trustModels WHERE ITEMNAME = ?";
        case "simulationResults" -> "SELECT * FROM simulationResults WHERE ITEMNAME = ?";
        default -> "";
      };

      PreparedStatement sqlStatement = dbConnection.prepareStatement(sqlQuery);
      sqlStatement.setString(1, objectName);
      ResultSet resultSet = sqlStatement.executeQuery();

      switch (resultSet.getString(2)) {
        case "class com.trustsim.synthesiser.AgentSystem" -> {
          output = gson.fromJson(resultSet.getString(3), AgentSystem.class);
        }
        case "class com.trustsim.simulator.EigenTrustModel" -> {
          output = gson.fromJson(resultSet.getString(3), EigenTrustModel.class);
        }
      }

      dbConnection.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return output;
  }

}
