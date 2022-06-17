import java.sql.*;

public class dbConnection {
    private static Connection conn;


    public static void setUpConnection() {
        // Connect to the database
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            System.out.println("\nOpened database successfully");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.out.println("\nFailed to connect to database\nExiting program...");
            System.exit(0);
        }
    }


    public static Connection getConnection() {
        return conn;
    }
}
