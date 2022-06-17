import java.sql.*;

public class dbConnection {
    private static Connection conn;


    public static void setUpConnection() {
        // Connect to the database
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";
        try {
            this.conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
            System.out.println("\nOpened database successfully");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return false;
    }


    public static Connection getConnection() {
        return conn;
    }
}
