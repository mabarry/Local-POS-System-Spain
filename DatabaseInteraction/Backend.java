import java.sql.*;
import java.io.*;
import java.util.*;

public class Backend {
    public static void textBoxResult(String foodID) {
        System.out.println(foodID + " returned from text box");

        // Connect to the database
        Connection conn = null;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        // Send an SQL query to the database
        try {
            Statement stmt = conn.createStatement();
            String sqlCommand = "SELECT * FROM FoodItems WHERE FoodID=" + foodID + ";";

            ResultSet result = stmt.executeQuery(sqlCommand);
            result.next();
            System.out.println(result.getString("foodName"));
            System.out.println(result.getString("foodQuantity"));
            System.out.println(result.getString("unitPrice"));
            
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        textBoxResult("11");
    }
}
