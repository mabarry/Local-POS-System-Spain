import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * This class houses the functions used to exchange information with a database.
 * @author Matthew Barry
 * @author Cameron Hoholik-Carlson
 * @author Harrison Kuhn
 * @author Kyle Owens
 */
public class DatabaseFunctions {
    /**
     * This establishes a database connection
     * @return conn Returns a connection to the database
     */
    public static Connection connectToDatabase() {
        // Connect to the database
        Connection conn = null;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            System.out.println("\nOpened database successfully");
            return conn;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }


    public static int getNumberOfColumns(String tableName) {
      int numCols = 0;
      //Setting the number of columns based on the table in the database
      switch (tableName) {
          case "FoodItems":
              numCols = 6;
              break;
          case "CustomerSaleLine":
              numCols = 5;
              break;
          case "CustomerOrder":
              numCols = 5;
              break;
          case "EmployeeList":
              numCols = 4;
              break;
          case "VendorOrder":
              numCols = 5;
              break;
          case "VendorBuyLine":
              numCols = 5;
              break;
      }

      return numCols;
    }


    public static String getPrimaryKey(String tableName) {
      String primaryKey = "";
      switch (tableName) {
          case "FoodItems":
              primaryKey = "foodID";
              break;
          case "CustomerSaleLine":
              primaryKey = "saleLineID";
              break;
          case "CustomerOrder":
              primaryKey = "customerOrderID";
              break;
          case "EmployeeList":
              primaryKey = "employeeID";
              break;
          case "VendorOrder":
              primaryKey = "vendorOrderID";
              break;
          case "VendorBuyLine":
              primaryKey = "vendorLineID";
              break;
      }
      return primaryKey;
    }

    public static int[] getMaxIDs() {
        Connection conn = connectToDatabase();
        int[] maxIDs = new int[6];
        
        try {
            Statement stmt = conn.createStatement();

            String sqlCommand = "SELECT MAX (foodID) FROM foodItems;";
            ResultSet result = stmt.executeQuery(sqlCommand);
            result.next();
            int foodID = Integer.parseInt(result.getString(1));
            maxIDs[0] = foodID;

            sqlCommand = "SELECT MAX (employeeID) FROM employeeList;";
            result = stmt.executeQuery(sqlCommand);
            result.next();
            int employeeID = Integer.parseInt(result.getString(1));
            maxIDs[1] = employeeID;

            sqlCommand = "SELECT MAX (vendorOrderID) FROM vendorOrder;";
            result = stmt.executeQuery(sqlCommand);
            result.next();
            int vendorOrderID = Integer.parseInt(result.getString(1));
            maxIDs[2] = vendorOrderID;

            sqlCommand = "SELECT MAX (customerOrderID) FROM customerOrder;";
            result = stmt.executeQuery(sqlCommand);
            result.next();
            int customerOrderID = Integer.parseInt(result.getString(1));
            maxIDs[3] = customerOrderID;

            sqlCommand = "SELECT MAX (vendorLineID) FROM vendorBuyLine;";
            result = stmt.executeQuery(sqlCommand);
            result.next();
            int vendorLineID = Integer.parseInt(result.getString(1));
            maxIDs[4] = vendorLineID;
            
            sqlCommand = "SELECT MAX (saleLineID) FROM customerSaleLine;";
            result = stmt.executeQuery(sqlCommand);
            result.next();
            int saleLineID = Integer.parseInt(result.getString(1));
            maxIDs[5] = saleLineID;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return maxIDs;
    }


    /**
     * This function gets data from a database given the table and ID
     * @param  tableName This is the name of the table in the database that the user wants to get data from.
     * @param  id This is the primary key that corresponds to the row the user wants to get data from.
     * @return data Returns an array of strings containing all the information contained in a single row.
     */
    public static String[] getDataFromPK(String tableName, String id) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement();

            String primaryKey = getPrimaryKey(tableName);
            int numCols = getNumberOfColumns(tableName);

            String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + primaryKey + "=" + id + ";";

            // Print out the results
            ResultSet result = stmt.executeQuery(sqlCommand);
            result.next();

            String[] data = new String[numCols];
            for (int i = 0; i < numCols; i++) {
                data[i] = result.getString(i+1);
            }

            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }


    public static String[][] getDataFromAttributes(String tableName, String[] attributeNames, String[] attributeValues) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement();

            // Create the conditional statemnt to check using the provided attribues
            String conditional = "";
            for (int i = 0; i < attributeNames.length - 1; i++) {
                conditional = conditional + attributeNames[i] + "=" + attributeValues[i] + " AND ";
            }
            conditional = conditional + attributeNames[attributeNames.length - 1] + "=" + attributeValues[attributeValues.length - 1];

            // Get the amount of expected rows and cols so we can store the data in an appropriately sized list
            //String sqlRowCommand = "SELECT COUNT(" + attributeNames[0] + ") FROM " + tableName + " WHERE " + conditional + ";";
            //ResultSet rowResult = stmt.executeQuery(sqlRowCommand);

            // Get a list of all the rows' data
            String sqlDataCommand = "SELECT * FROM " + tableName + " WHERE " + conditional + ";";
            System.out.println(sqlDataCommand);
            ResultSet result = stmt.executeQuery(sqlDataCommand);

            // Get the amount of expected rows and cols so we can store the data in an appropriately sized list
            result.first();
            int firstRow = result.getRow();
            result.last();
            int lastRow = result.getRow();
            int numRows = lastRow - firstRow;

            int numCols = getNumberOfColumns(tableName);

            // Retrieve all the row information
            String[][] data = new String[numRows][numCols];

            // For each row returned, store the data in each column in our 2D array
            int currRow = 0;

            result.beforeFirst();
            while (result.next()) {
                for (int i = 0; i < numCols; i++) {
                    data[currRow][i] = result.getString(i+1);
                }
                currRow++;
            }

            return data;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }


    /**
     * This function adds a row of data passed in as parameters to a given table in a database.
     * @param tableName This is the name of the table that the user wants to add data to.
     * @param attributes[] This is the array of data that tjhe user want to add to the table in the database.
     */
    public static void addData(String tableName, String attributes[]) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String sqlCommand = "INSERT INTO " + tableName + " VALUES(";
            for (int i = 0; i < attributes.length - 1; i++) {
                sqlCommand = sqlCommand + attributes[i] + ", ";
            }
            sqlCommand = sqlCommand + attributes[attributes.length - 1] + ");";
            System.out.println(sqlCommand);

            // Send SQL statement to database
            int result = stmt.executeUpdate(sqlCommand);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /**
     * This function edits existing data in a table within a database.
     * @param tableName This is the name of the table that the user wants to edit data in.
     * @param id This is the primary key of the row that the user wants to edit.
     * @param attribute This is the attribute of the row that the user wants to edit.
     * @param newData This is the value of the attribute that will be replacing the existing value in the row.
     */
    public static void editData(String tableName, String id, String attribute, String newData) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String primaryKey = getPrimaryKey(tableName);

            String sqlCommand = "UPDATE " + tableName + " SET " + attribute + "=" + newData + " WHERE " + primaryKey + "=" + id + ";";

            // Send SQL statement to database
            int result = stmt.executeUpdate(sqlCommand);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return;
    }


    /**
     * This function deletes a row from a table in a database
     * @param tableName This is the name of the table that the user wants to delete a row from.
     * @param id This is the primary key of the row that the user wants to delete.
     */
    public static void deleteData(String tableName, String id) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String primaryKey = "";
            switch (tableName) {
                case "FoodItems":
                    primaryKey = "foodID";
                    break;
                case "CustomerSaleLine":
                    primaryKey = "saleLineID";
                    break;
                case "CustomerOrder":
                    primaryKey = "customerOrderID";
                    break;
                case "EmployeeList":
                    primaryKey = "employeeID";
                    break;
                case "VendorOrder":
                    primaryKey = "vendorOrderID";
                    break;
                case "VendorBuyLine":
                    primaryKey = "vendorLineID";
                    break;
            }

            String sqlCommand = "DELETE FROM " + tableName + " WHERE " + primaryKey + "=" + id + ";";

            // Send SQL statement to database
            int result = stmt.executeUpdate(sqlCommand);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    /**
     * This function is used to test the backend.
     * @param args[] This is an array of strings used for testing purposes.
     */
    public static void main(String args[]) {
        System.out.println("STARTING PROGRAM");

        /* ADD TESTS HERE */

        System.out.println("FINISHED");
    }
}
