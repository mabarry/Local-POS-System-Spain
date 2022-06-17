import java.sql.*;
import java.io.*;

/*
CSCE 315
9-27-2021 Lab
 */
public class FoodItemsCSVReader {

  //Commands to run this script
  //This will compile all java files in this directory
  //javac *.java
  //This command tells the file where to find the postgres jar which it needs to execute postgres commands, then executes the code
  //Windows: java -cp ".;postgresql-42.2.8.jar" jdbcpostgreSQL
  //Mac/Linux: java -cp ".:postgresql-42.2.8.jar" jdbcpostgreSQL

  //MAKE SURE YOU ARE ON VPN or TAMU WIFI TO ACCESS DATABASE
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
     } catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName()+": "+e.getMessage());
        System.exit(0);
     }

     System.out.println("Opened database successfully");

     try {

       String line;
       String delimiter = ",";
       BufferedReader csvReader = new BufferedReader(new FileReader("FoodItems.csv"));

       //create a statement object
       Statement stmt = conn.createStatement();
       int batchCounter = 0;
       int[] result = {};
       //Running a query
       //TODO: update the sql command here
       while ((line = csvReader.readLine()) != null) {
        batchCounter++;
        String[] currentLineBeingRead = line.split(delimiter);
        String sqlStatement = "INSERT INTO fooditems (foodID, foodName, unitPrice, foodQuantity, storageType) VALUES(" + currentLineBeingRead[0] + ", '" + 
                              currentLineBeingRead[1] + "', " + currentLineBeingRead[2] + ", " + currentLineBeingRead[3] + ", '" + currentLineBeingRead[4] + "');";

        stmt.addBatch(sqlStatement);

       //send statement to DBMS
       //This executeQuery command is useful for data retrieval
       //ResultSet result = stmt.executeQuery(sqlStatement);
       //OR 
       //This executeUpdate command is useful for updating data
       //int result = stmt.executeUpdate(sqlStatement);
       //OUTPUT
       //You will need to output the results differently depeninding on which function you use

       }

       result = stmt.executeBatch();
       stmt.clearBatch();

       System.out.println("--------------------Query Results--------------------");
       for(int i : result) {
         System.out.println(i);
       }
       
   } catch (Exception e){
       e.printStackTrace();
       System.err.println(e.getClass().getName()+": "+e.getMessage());
       System.exit(0);
   }

    //closing the connection
    try {
      conn.close();
      System.out.println("Connection Closed.");
    } catch(Exception e) {
      System.out.println("Connection NOT Closed.");
    }//end try catch
  }//end main
}//end Class
