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
     * Gets the number of columns(attributes) in a given table
     * @param  tableName The name of the table you want to get how many colunns are in
     * @return An integer of the number of columns(attributes) in the provided table
     */
    public static int getNumberOfColumns(String tableName) {
      int numCols = 0;
      //Setting the number of columns based on the table in the database
      switch (tableName.toUpperCase()) {
          case "FOODITEMS":
              numCols = 6;
              break;
          case "CUSTOMERSALELINE":
              numCols = 5;
              break;
          case "CUSTOMERORDER":
              numCols = 5;
              break;
          case "EMPLOYEELIST":
              numCols = 3;
              break;
          case "VENDORORDER":
              numCols = 5;
              break;
          case "VENDORBUYLINE":
              numCols = 5;
              break;
      }

      return numCols;
    }

    /**
     * Gives the name of the primary key attribute for a given table name
     * @param  tableName The name of the table to get the primary key attributes from
     * @return A string of the attribute name that contains the primary key
     */
    public static String getPrimaryKey(String tableName) {
      String primaryKey = "";
      switch (tableName.toUpperCase()) {
          case "FOODITEMS":
              primaryKey = "foodID";
              break;
          case "CUSTOMERSALELINE":
              primaryKey = "saleLineID";
              break;
          case "CUSTOMERORDER":
              primaryKey = "customerOrderID";
              break;
          case "EMPLOYEELIST":
              primaryKey = "employeeID";
              break;
          case "VENDORORDER":
              primaryKey = "vendorOrderID";
              break;
          case "VENDORBUYLINE":
              primaryKey = "vendorLineID";
              break;
      }
      return primaryKey;
    }

    /**
     * Get the last ID numbers for all of the database tables
     * @return An integer array containing all the current max ID values in the form
     * [foodID, employeeID, vendorOrderID, customerOrderID, vendorBuyLine, customerSaleLines]
     */
    public static int[] getMaxIDs() {
        Connection conn = dbConnection.getConnection();
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
     * WIll get the ID of the first order that was taken on a given day
     * @param  startDate The date that you want to get the first order of
     * @return A string represntation of the day's first order ID number
     */
    public static String getStartOrderIDFromDates(String startDate) {
        // Connect to the database
        Connection conn = dbConnection.getConnection();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String primaryKey = "CustomerOrderID";
            int numCols = 5;

            String sqlCommand = "SELECT * FROM CustomerOrder WHERE customerOrderDate=\'" + startDate + "\';";

            ResultSet result = stmt.executeQuery(sqlCommand);
            result.first();

            return result.getString(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }

    /**
     * Will get the ID of the last order that was taken on a given day
     * @param  endDate The date that you want to get the last order of
     * @return A string represntation of the day's last order
     */
    public static String getEndOrderIDFromDates(String endDate) {
        // Connect to the database
        Connection conn = dbConnection.getConnection();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String primaryKey = "CustomerOrderID";
            int numCols = 5;

            String sqlCommand = "SELECT * FROM CustomerOrder WHERE customerOrderDate=\'" + endDate + "\';";

            ResultSet result = stmt.executeQuery(sqlCommand);
            result.last();

            return result.getString(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return null;
    }


    /**
     * This function gets data from a database given the table and ID
     * @param  tableName This is the name of the table in the database that the user wants to get data from.
     * @param  id This is the primary key that corresponds to the row the user wants to get data from.
     * @return data Returns an array of strings containing all the information contained in a single row.
     */
    public static String[] getDataFromPK(String tableName, String id) {
        // Connect to the database
        Connection conn = dbConnection.getConnection();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement();

            String primaryKey = getPrimaryKey(tableName);
            int numCols = getNumberOfColumns(tableName);

            String sqlCommand = "SELECT * FROM " + tableName + " WHERE " + primaryKey + "=\'" + id + "\';";
            //System.out.println("\n\n\nABOUT TO EXECUTE:\t\t" + sqlCommand);

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

    /**
     * Will get all the rows that match all of the provided attribute values from a given table
     * @param  tableName The table name you want to get data from
     * @param  attributeNames The names of the attributes you want to perform the check on
     * @param  attributeValues The values of all the attributes you wanted to test that must match for the row's data to be retrieved
     * @return A 2D array containing all of the rows, and all of the rows' data in the form result[row][column]
     */
    public static String[][] getDataFromAttributes(String tableName, String[] attributeNames, String[] attributeValues) {
        // Connect to the database
        Connection conn = dbConnection.getConnection();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Create the conditional statement to check using the provided attribues
            String conditional = "";
            for (int i = 0; i < attributeNames.length - 1; i++) {
                conditional = conditional + attributeNames[i] + "=\'" + attributeValues[i] + "\' AND ";
            }
            conditional = conditional + attributeNames[attributeNames.length - 1] + "=\'" + attributeValues[attributeValues.length - 1];

            // Get a list of all the rows' data
            String sqlDataCommand = "SELECT * FROM " + tableName + " WHERE " + conditional + "\';";
            //System.out.println(sqlDataCommand);
            ResultSet result = stmt.executeQuery(sqlDataCommand);

            // Get the amount of expected rows and cols so we can store the data in an appropriately sized list
            result.next();
            int firstRow = result.getRow();
            result.last();
            int lastRow = result.getRow();
            int numRows = lastRow - firstRow + 1;

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
        Connection conn = dbConnection.getConnection();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String sqlCommand = "INSERT INTO " + tableName + " VALUES(\'";
            for (int i = 0; i < attributes.length - 1; i++) {
                sqlCommand = sqlCommand + attributes[i] + "\', \'";
            }
            sqlCommand = sqlCommand + attributes[attributes.length - 1] + "\');";

            // Send SQL statement to database
            int result = stmt.executeUpdate(sqlCommand);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }
    }

    /**
     * Counts the number of rows that contain a specific value for a given attribute
     * @param  tableName The name of the table to count rows in
     * @param  columnName The attribute names you want to check
     * @param  attributeValue The attribute value of rows that you want to count
     * @return A string represntation of the number of rows matching the attribute value
     */
    public static String countRows(String tableName, String columnName, String attributeValue) {
  	     Connection conn = dbConnection.getConnection();

            try {
                // Send an SQL query to the database
                Statement stmt = conn.createStatement();

                String sqlCommand = "SELECT COUNT(" + columnName + ") FROM " + tableName + " WHERE " + columnName + "=\'" + attributeValue + "\';";
                //System.out.println(sqlCommand);

                ResultSet result = stmt.executeQuery(sqlCommand);
                result.next();
                String numberOfRows = result.getString(1);

                return numberOfRows;
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            return null;
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
        Connection conn = dbConnection.getConnection();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String primaryKey = getPrimaryKey(tableName);

            String sqlCommand = "UPDATE " + tableName + " SET " + attribute + "=\'" + newData + "\' WHERE " + primaryKey + "=\'" + id + "\';";
            // System.out.println("SQL Statement = " + sqlCommand);

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
        Connection conn = dbConnection.getConnection();

        try{
            // Create the SQL statement
            Statement stmt = conn.createStatement();

            String primaryKey = getPrimaryKey(tableName);

            String sqlCommand = "DELETE FROM " + tableName + " WHERE " + primaryKey + "=\'" + id + "\';";

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
     * Will delete all rows in a given table that have a certain attribute value
     * @param tableName The table name you wish to delete from
     * @param columnName The attribute name you wish to use to perfrom the check
     * @param attributeValue The attribute value of the rwos you want to delete
     */
    public static void deleteMultipleRows(String tableName, String columnName, String attributeValue) {
        Connection conn = dbConnection.getConnection();

        try {
            Statement stmt = conn.createStatement();
            String sqlCommand = "DELETE FROM " + tableName + " WHERE " + columnName + "=\'" + attributeValue + "\';";

            // Send SQL statement to database
            int result = stmt.executeUpdate(sqlCommand);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
}
