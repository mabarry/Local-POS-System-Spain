import java.sql.*;
import java.io.*;
import java.util.*;

/* Our Primary Key ID Layout: */
// foodID: 1 to 100
// EmployeeID: 101 to 200

// VendorOrderID: 1,001 to 10,000
// CustomerOrderID: 10,001 to 20,000

// buyLineID: 100,000 to 1,000,000
// saleLineID: 1,000,001 to 2,000,000


// This file will populate the CustomerOrder and CustomerSaleLine database tables.
// If we change the database names within the file, we can also populate the vendorOrder and vendorBuyLine tables.
public class GeneratePastOrders {
    static String sqlQuery(String command, String needResult) {
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

            if (needResult.length() > 0) {
                ResultSet result = stmt.executeQuery(sqlStatement);
                
                while (result.next()) {
                    return result.getString(needResult);
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


    static double GenerateOrder(int customerID, String orderDate, int saleLineID) {
        int randomFoodID;
        double randomMass;
        double saleLinePrice;
        double orderTotal = 0;
        int customerOrderId = customerID;
        
        // Generates a single random order        
        Random rand = new Random();
        int numOfSaleLines = rand.nextInt(10) + 1;

        for (int i = 0; i < numOfSaleLines; i++) {
            // Generate an ID between 1 and 55
            // Generate a mass between 0.25 and 3.75
            randomFoodID = rand.nextInt(55) + 1;
            randomMass = 1.5 * rand.nextDouble() + 0.25;
            
            // Truncate randomMass to 2 decimal places
            randomMass = ( (double) ((int)(randomMass * 100) ) ) / 100;

            // Query for unit price given the foodID
            double unitPrice = Double.parseDouble(sqlQuery("SELECT unitPrice FROM FoodItems WHERE foodID='" + randomFoodID + "';", "unitPrice"));
            
            saleLinePrice = unitPrice * randomMass;
            orderTotal += saleLinePrice;
            // Add saleLine to database
            sqlQuery("INSERT INTO CustomerSaleLine(saleLineID, customerOrderID, foodID, saleLinePrice, saleLineQuantity) VALUES(" + 
            saleLineID + ", " + customerOrderId + ", " + randomFoodID + ", " + saleLinePrice + ", " + randomMass + ");", "");
        
            saleLineID++;
        }

        // Add the order to CustomerOrder database
        sqlQuery("UPDATE CustomerOrder SET customerOrderTotal = " + orderTotal + "WHERE customerOrderID = '" + customerID + "';", "");

        return orderTotal;
    }


    public static void main(String args[]) {
        Random rand = new Random();
        int customerID = 10001;
        int dayOfTheMonth = 10;
        String date = "2022-06-" + dayOfTheMonth;
        String[] allPaymentMethods = {"Cash", "Credit", "Debit"};
        double dailyTotal = 0;
        double salesGoalTotal = 0;
        int startSaleLineID = 1000001;

        while(salesGoalTotal < 18000) {
            int employeeID = rand.nextInt(4) + 101;
            String paymentMethod = allPaymentMethods[rand.nextInt(3)];

            sqlQuery("INSERT INTO CustomerOrder(customerOrderID, customerOrderDate, customerOrderTotal, paymentMethod, employeeID) VALUES(" +
            customerID + ", '" + date + "', 0.01, '" + paymentMethod + "', " + employeeID + ");", "");

            double tempOrderTotal = GenerateOrder(customerID, date, startSaleLineID);
            dailyTotal += tempOrderTotal;
            salesGoalTotal += tempOrderTotal;

            if(dailyTotal > 1000) {
                dayOfTheMonth += 1;
                dailyTotal = 0;
                date = "2022-06-" + dayOfTheMonth;
            }

            customerID++;

            startSaleLineID = 1 + Integer.valueOf(sqlQuery("SELECT * FROM CustomerSaleLine WHERE saleLineID=(SELECT max(saleLineID) FROM CustomerSaleLine);", "saleLineID"));
        }
    }
}
