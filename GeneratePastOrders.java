import java.sql.*;
import java.io.*;
import java.util.*;


// foodID: 1 to 100
// EmployeeID: 101 to 200
// saleLineID: 201 to 600
// buyLineID: 601 to 1,000
// VendorOrderID: 1,001 to 100,000
// CustomerOrderID: 100,001 to 1,000,000







public class GeneratePastOrders {
    static String sqlQuery(String command, boolean needResult) {
        Connection conn = null;
        String dbConnectionString = "jdbc:postgresql://csce-315-db.engr.tamu.edu/csce315950_1db";

        // Establishes connection
        try {
            conn = DriverManager.getConnection(dbConnectionString, dbSetup.user, dbSetup.pswd);
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        // Send query statement
        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = command;

            if (needResult) {
                ResultSet result = stmt.executeQuery(sqlStatement);
                
                while (result.next()) {
                    return result.getString("unitPrice");
                }
            }
            else {
                int result = stmt.executeUpdate(sqlStatement);
            }

            return "";
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return "failed";
    }


    static double GenerateOrder(int customerID, String orderDate) {
        int randomFoodID;
        double randomMass;
        double saleLinePrice;
        double orderTotal = 0;
        int saleLineID = 1000;
        int customerOrderId = customerID;
        
        // Generates a single random order        
        Random rand = new Random();
        int numOfSaleLines = rand.nextInt(10) + 1;

        for (int i = 0; i < numOfSaleLines; i++) {
            // Generate an ID between 1 and 55
            // Generate a mass between 0.25 and 3.75
            randomFoodID = rand.nextInt(55) + 1;
            randomMass = 3.5 * rand.nextDouble() + 0.25;
            
            // Truncate randomMass to 2 decimal places
            randomMass = ( (double) ((int)(randomMass * 100) ) ) / 100;

            // Query for unit price given the foodID
            double unitPrice = Double.parseDouble(sqlQuery("SELECT unitPrice FROM FoodItems WHERE foodID='" + randomFoodID + "';", true));
            
            saleLinePrice = unitPrice * randomMass;
            orderTotal += saleLinePrice;
            // Add saleLine to database
            sqlQuery("INSERT INTO CustomerSaleLine(saleLineID, customerOrderID, foodID, saleLinePrice, saleLineQuantity) VALUES(" + 
            saleLineID + ", " + customerOrderId + ", " + randomFoodID + ", " + saleLinePrice + ", " + randomMass + ");", false);
        
            saleLineID++;
        }

        // Add the order to CustomerOrder database
        sqlQuery("UPDATE CustomerOrder SET customerOrderTotal = " + orderTotal + "WHERE customerOrderID = '" + customerID + "';", false);

        return orderTotal;
    }


    public static void main(String args[]) {
        Random rand = new Random();
        int customerID = 1;
        String date = "2022-06-10";
        int employeeID = rand.nextInt(4) + 1;
        String[] allPaymentMethods = {"Cash", "Credit", "Debit"};
        String paymentMethod = allPaymentMethods[rand.nextInt(3)];
        double dailyTotal = 0;

        // while(dailyTotal < 1100) {

        // }
        sqlQuery("INSERT INTO CustomerOrder(customerOrderID, customerOrderDate, customerOrderTotal, paymentMethod, employeeID) VALUES(" +
        customerID + ", '" + date + "', 0.01, '" + paymentMethod + "', " + employeeID + ");", false);

        GenerateOrder(customerID, date);
    }
}
