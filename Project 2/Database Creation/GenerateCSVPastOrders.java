import java.sql.*;
import java.io.*;
import java.util.*;


/* Our Primary Key ID Layout: */
// foodID: 1 to 100
// EmployeeID: 101 to 200

// VendorOrderID: 1,001 to 10,000
// CustomerOrderID: 10,001 to 20,000

// buyLineID: 100,001 to 1,000,000
// saleLineID: 1,000,001 to 2,000,000


// This file will populate the CustomerOrder and CustomerSaleLine database tables.
// If we change the database names within the file, we can also populate the vendorOrder and vendorBuyLine tables.
public class GenerateCSVPastOrders {
    static int customerOrderID = 10001;
    static int saleLineID = 1000001;


    static String sqlQuery(Connection conn, String command, String needResult) {
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


    static String[] GenerateSaleLine(Connection conn, String orderDate) {
        String[] saleLineInfo = {"", ""};

        // Attributes for CustomerSaleLane
        int randomFoodID;
        double saleLinePrice;
        double randomMass;

        // Generate an ID between 1 and 55
        // Generate a mass between 0.25 and 1.75 kg
        Random rand = new Random();
        randomFoodID = rand.nextInt(55) + 1;
        randomMass = 1.5 * rand.nextDouble() + 0.25;
        
        // Truncate randomMass to 2 decimal places
        randomMass = ( (double) ((int)(randomMass * 100) ) ) / 100;

        // Query for unit price given the foodID
        double unitPrice = Double.parseDouble(sqlQuery(conn, "SELECT unitPrice FROM FoodItems WHERE foodID='" + randomFoodID + "';", "unitPrice"));
        
        saleLinePrice = unitPrice * randomMass;
        saleLinePrice = ( (double) ((int)(saleLinePrice * 100) ) ) / 100;

        // Add saleLine to the CustomerSaleLine CSV
        saleLineInfo[0] = (saleLineID + ", " + customerOrderID + ", " + randomFoodID + ", " + saleLinePrice + ", " + randomMass + "\n");
        saleLineInfo[1] = String.valueOf(saleLinePrice);

        return saleLineInfo;
    }


    // Populate CustomerOrder and CustomerSaleLine CSV files
    public static void main(String args[]) {
        // Establish connection
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

        // Start generating orders for each day
        int day = 13;
        int month = 6;
        String date = "2022-0" + month + "-" + day;

        double dailyTotal = 0;
        int numDays = 0;

        try {
            PrintWriter saleLineWriter = new PrintWriter("CustomerSaleLine.csv");
            PrintWriter orderWriter = new PrintWriter("CustomerOrder.csv");
            
            String saleLineToWrite;
            String orderToWrite;
            int dailyTotalGoal = 1000;
            Random rand = new Random();
            
            // Start generating all the orders
            while(numDays < 21) {
                // Generate all the sale lines
                double orderTotal = 0;
                int numOfSaleLines = rand.nextInt(10) + 1;
                
                for (int i = 0; i < numOfSaleLines; i++) {
                    String[] saleLineInfo = GenerateSaleLine(conn, date);

                    saleLineToWrite = saleLineInfo[0];
                    orderTotal += Double.parseDouble(saleLineInfo[1]);

                    saleLineWriter.write(saleLineToWrite);
                    saleLineID++;
                }

                // Write the order to csv
                String[] allPaymentMethods = {"Cash", "Credit", "Debit"};
                String paymentMethod = allPaymentMethods[rand.nextInt(3)];

                int randomEmployeeID = rand.nextInt(4) + 101;

                orderToWrite = (customerOrderID + ", " + date + ", " + orderTotal + ", " + paymentMethod + ", " + randomEmployeeID + "\n");
                orderWriter.write(orderToWrite);

                // Continue to generate new orders if the daily total is less than 1000
                dailyTotal += orderTotal;
                
                if(dailyTotal > dailyTotalGoal) {
                    System.out.println("Day " + (numDays+1) + " finished.");

                    dailyTotal = 0;
                    numDays++;

                    // Increase the daily total for the last two days to make them count as festivals
                    if (numDays > 19) {
                        dailyTotalGoal = 3000;
                    }
                    
                    // Update the date variable to a new day
                    day--;
                    if (day == 0) {
                        day = 31;
                        month--;
                    }
                    
                    if (day < 10) {
                        date = "2022-0" + month + "-0" + day;
                    }
                    else {
                        date = "2022-0" + month + "-" + day;
                    }
                }

                customerOrderID++;
            }

            saleLineWriter.close();
            orderWriter.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
