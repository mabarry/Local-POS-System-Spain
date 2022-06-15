
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
public class Backend {
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

    /**
     * This function gets data from a database given the table and ID
     * @param  tableName This is the name of the table in the database that the user wants to get data from.
     * @param  id This is the primary key that corresponds to the row the user wants to get data from.
     * @return data Returns an array of strings containing all the information contained in a single row.
     */
    public static String[] getData(String tableName, String id) {
        // Connect to the database
        Connection conn = connectToDatabase();

        try {
            // Send an SQL query to the database
            Statement stmt = conn.createStatement();

            String primaryKey = "";
            int numCols = 0;
            switch (tableName) {
                case "FoodItems":
                    primaryKey = "foodID";
                    numCols = 6;
                    break;
                case "CustomerSaleLine":
                    primaryKey = "saleLineID";
                    numCols = 5;
                    break;
                case "CustomerOrder":
                    primaryKey = "customerOrderID";
                    numCols = 5;
                    break;
                case "EmployeeList":
                    primaryKey = "employeeID";
                    numCols = 4;
                    break;
                case "VendorOrder":
                    primaryKey = "vendorOrderID";
                    numCols = 5;
                    break;
                case "VendorBuyLine":
                    primaryKey = "vendorLineID";
                    numCols = 5;
                    break;
            }

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
