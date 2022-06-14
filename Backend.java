import java.sql.*;
import java.io.*;
import java.util.*;

public class Backend {
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



    public static void main(String args[]) {
        System.out.println("STARTING PROGRAM");

        /* ADD TESTS HERE */

        System.out.println("FINISHED");
    }
}
