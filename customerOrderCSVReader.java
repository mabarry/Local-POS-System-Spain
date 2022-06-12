import java.sql.*;
import java.io.*;

/* CSCE 315 6-10-2022 */

public class jdbcpostgreSQL {

  public static void main(String args[]) {
    //Building the connection with your credentials
    Connection conn = null;
    String teamNumber = "1";
    String sectionNumber = "950";
    String dbName = "csce315" + sectionNumber + "_" + teamNumber + "db";
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/" + dbName;

    //Connecting to the database
    try {
      conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
    } 
    catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
    }

    System.out.println("Opened database successfully");

    try {

      String line;
      String delimiter = ",";
      BufferedReader csvReader = new BufferedReader(new FileReader("xxxxxxxxxx.csv"));

      //create a statement object
      Statement stmt = conn.createStatement();
      int batchCounter = 0;
      int[] result = {};

      //Running a query
      while ((line = csvReader.readLine()) != null) {
        batchCounter++;
        String[] currentLineBeingRead = line.split(delimiter);
        String sqlStatement = "INSERT INTO customerOrders (customerOrderID, customerOrderDate, customerOrderTotal, paymentMethod, employeeID) VALUES(" + 
                              currentLineBeingRead[0] + ", '" + currentLineBeingRead[1] + "', " + currentLineBeingRead[2] + ", " + currentLineBeingRead[3] + 
                              ", '" + currentLineBeingRead[4] + "');";

        stmt.addBatch(sqlStatement);

      }

      result = stmt.executeBatch();
      stmt.clearBatch();

      System.out.println("--------------------Query Results--------------------");
      for(int i : result) {
        System.out.println(i);
      }
       
   } 
   catch (Exception e){
      e.printStackTrace();
      System.err.println(e.getClass().getName()+": "+e.getMessage());
      System.exit(0);
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } 
    catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    } //end try catch

  } //end main

} //end Class
