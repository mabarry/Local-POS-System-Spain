import java.sql.*;
import java.io.*;
import java.util.*;


// foodID: 1 to 100
// EmployeeID: 101 to 200
// saleLineID: 201 to 600
// buyLineID: 601 to 1,000
// VendorOrderID: 1,001 to 100,000
// CustomerOrderID: 100,001 to 1,000,000


String sqlQuery(String command) {
    Connection conn = null;
    String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";

    // Establishes connection
    try {
        conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.password);
    } 
    catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }

    // Send query statement
    try {
        Statement stmt = conn.createStatement();
        String sqlStatement = command;

        ResultSet result = null;
        result = stmt.executeQuery(sqlStatement);

        System.out.println(result);
    }
    catch (Exception e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
    }

    return "" + result;
}


void GenerateOrder(int CustomerOrderID) {
    double randomFoodID;
    double randomMass;
    double saleLinePrice;
    double orderTotal;
    int saleLineID = 1000;
    int customerOrderId = CustomerOrderID;
    
    // Generates a single random order        
    Random rand = new Random();
    int numOfSaleLines = rand.nextInt(10) + 1;
    double[][] order = new double[numOfSaleLines][2];

    for (int i = 0; i < numOfSaleLines; i++) {
        // Generate an ID between 1 and 55
        // Generate a mass between 0.25 and 3.75
        randomFoodID = (double)(rand.nextInt(55) + 1);
        randomMass = 3.5 * rand.nextDouble() + 0.25;
        
        // Truncate randomMass to 2 decimal places
        randomMass = ( (double) ((int)(randomMass * 100) ) ) / 100;

        order[i][0] = randomFoodID;
        order[i][1] = randomMass;

        System.out.println("[" + order[i][0] + ", " + order[i][1] + "]");

        // Query for unit price given the foodID
        unitPrice = sqlQuery("SELECT unitPrice FROM FoodItems WHERE foodID='" + foodID + "';");

        saleLinePrice = unitPrice * randomMass;
        orderTotal += lineTotal;

        // Add saleLine to database
        sqlQuery("INSERT INTO CustomerSaleLine(saleLineID, customerOrderID, foodID, saleLinePrice, saleLineQuantity) VALUES(" + 
        saleLineID + ", " + customerOrderId + ", " + randomFoodID + ", " + saleLinePrice + ", " + randomMass + ");");
    
        saleLineID++;
    }

    // TODO: Add order to database
}


public class GeneratePastOrders {
    public static void main(String args[]) {
        /* Notes and Assumptions:
        *    - Open 6 days per week, and 2 days are reserved for festivals
        */

        // [[id, quantity], ...]

        GenerateOrder();

        

    }
}
