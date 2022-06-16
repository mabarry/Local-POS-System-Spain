  import javax.swing.*;
  import javax.swing.border.*;
  import javax.swing.table.DefaultTableModel;
  import javax.swing.table.TableColumnModel;
  import java.awt.*;
  import java.awt.event.ActionEvent;
  import java.awt.event.ActionListener;
  import java.io.*;
  import java.util.*;

  /**
   * This class holds all the function that link the GUI buttons to the back-end functions that can interact with the database
   * @author Matthew Barry
   * @author Cameron Hoholik-Carlson
   * @author Harrison Kuhn
   * @author Kyle Owens
   */
  public class Interact {
      /**
       * Dummy function to be implemented later
       * Will be used to notify the GUI to bring up an error window
       */
      public static void RAISEERROR() {
          System.out.println("error");
      }

      /**
       * Used to initialize all the IDs to their most up to date values
       * @param employeeID The ID for the employee working the system
       */
      public static void initializer(String employeeID) {
          int[] allIDs = DatabaseFunctions.getMaxIDs();

          IDTracker.setAllIDs(allIDs[0], allIDs[1], allIDs[2], allIDs[3], allIDs[4], allIDs[5]);

          // Increment currCustomerOrderID to prepare for the next order
          IDTracker.incCustomerOrderID();
          IDTracker.incVendorOrderID();

          // Create new CustomerOrderID
          String date = getDate();
          String[] newOrderAttributesCustomer = {String.valueOf(IDTracker.getCustomerOrderID()), date, "0.01", "cash", employeeID};
          DatabaseFunctions.addData("CustomerOrder", newOrderAttributesCustomer);

          String[] newOrderAttributesVendor = {String.valueOf(IDTracker.getVendorOrderID()), "Bubba Gump", date, "0.01", employeeID};
          DatabaseFunctions.addData("VendorOrder", newOrderAttributesVendor);
      }



      /**
       * Will give the current date
       * @return A string of the date in 'YYYY-MM-DD' format
       */
      public static String getDate() {
          Calendar calendar = Calendar.getInstance();

          String year = String.valueOf(calendar.get(Calendar.YEAR));

          String month;
          if (calendar.get(Calendar.MONTH) < 10) {
              month = "0" + String.valueOf(calendar.get(Calendar.MONTH));
          }
          else {
              month = String.valueOf(calendar.get(Calendar.MONTH));
          }

          String day;
          if (calendar.get(Calendar.DATE) < 10) {
              day  = "0" + String.valueOf(calendar.get(Calendar.DATE));
          }
          else {
              day = String.valueOf(calendar.get(Calendar.DATE));
          }

          String date = year + "-" + month + "-" + day;
          return date;
      }

      /**
       * Will retrieve data about a given food item when the an item is searched in the search bar
       * @param  id The FoodID that was typed into the search bar
       * @return A String array that contains all the row information about the passed food item:
       * [foodID, foodName, unitPrice, foodQuantity, storageType, packaged]
       */
      public static String[] itemSearchDisplay(String foodID) {
          String[] foodData = DatabaseFunctions.getDataFromPK("FoodItems", foodID);

          return foodData;
      }

      /**
       * Will add a new sale line to the current order when the "Add To Order" button is pushed
       * @param  foodID The ID number of the food being added to the order
       * @param  customerQuantity The quantity of the food item the customer is buying
       * @return A String array that contains all the row information about the new sale line entity:
       * [saleLineID, customerOrderID, foodID, saleLinePrice, saleLineQuantity]
       */
      public static String[] addToSaleLine(String foodID, String customerQuantity) {
          // Fetch data about the given food item
          String[] foodData = DatabaseFunctions.getDataFromPK("FoodItems", foodID);
          double inventoryQuantity = Double.parseDouble(foodData[3]);
          double unitPrice = Double.parseDouble(foodData[2]);

          // If there is not enough of the food, notify the employee by raising an error
          if (Double.parseDouble(customerQuantity) > inventoryQuantity) {
              RAISEERROR();
              return null;
            }

          // Assuming there is a customer order already created,
          // Subtract quantity from inventory
          double newQuantity = inventoryQuantity - Double.parseDouble(customerQuantity);
          newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;
          System.out.println("Editing with new quantity: " + newQuantity);
          DatabaseFunctions.editData("FoodItems", foodID, "foodQuantity", String.valueOf(newQuantity));

          // Create a new sale line and add it to the database
          IDTracker.incSaleLineID();
          double saleLinePrice = Double.parseDouble(customerQuantity) * unitPrice;
          saleLinePrice = ( (double) ((int)(saleLinePrice* 100) ) ) / 100;
          String[] saleLineData = {String.valueOf(IDTracker.getSaleLineID()), String.valueOf(IDTracker.getCustomerOrderID()), foodID, String.valueOf(saleLinePrice), customerQuantity};
          DatabaseFunctions.addData("CustomerSaleLine", saleLineData);

          // Update the total price of the CustomerOrder
         // System.out.println("\n\n\nADDED SALE LINE");
          String[] orderData = DatabaseFunctions.getDataFromPK("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()));
          //System.out.println("\n\n\ngetDataFromPK PASSED");
          double newOrderTotal = Double.parseDouble(orderData[2]) + saleLinePrice;
          newOrderTotal = ( (double) ((int)(newOrderTotal* 100) ) ) / 100;
          //System.out.println("\n\n\nnewOrderTotal PASSED");
          DatabaseFunctions.editData("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()), "customerOrderTotal", String.valueOf(newOrderTotal));
          //System.out.println("\n\n\neditData PASSED");
          // Return the sale line info to the front-end
          return saleLineData;
      }

      /**
       * Will add a new buy line for the current vendor order when the "Add to Order" button is pushed
       * @param  foodID The ID number for the food being added to the order
       * @param  customerQuantity The quantity of food you want to order from the vendor
       * @return Returns a String array of all the info of the newly added buy line
       */
      public static String[] addToBuyLine(String foodID, String customerQuantity) {
          // Fetch data about the given food item
          String[] foodData = DatabaseFunctions.getDataFromPK("FoodItems", foodID);
          double inventoryQuantity = Double.parseDouble(foodData[3]);
          double unitPrice = Double.parseDouble(foodData[2]);

          // If there is not enough of the food, notify the employee by raising an error
          if (Double.parseDouble(customerQuantity) > inventoryQuantity) {
              RAISEERROR();
              return null;
            }

          // Assuming there is a customer order already created,
          // Subtract quantity from inventory
          double newQuantity = inventoryQuantity + Double.parseDouble(customerQuantity);
          newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;
          DatabaseFunctions.editData("FoodItems", foodID, "foodQuantity", String.valueOf(newQuantity));

          // Create a new sale line and add it to the database
          IDTracker.incBuyLineID();
          double buyLinePrice = Double.parseDouble(customerQuantity) * unitPrice;
          buyLinePrice = ( (double) ((int)(buyLinePrice * 100) ) ) / 100;
          String[] buyLineData = {String.valueOf(IDTracker.getSaleLineID()), String.valueOf(IDTracker.getVendorOrderID()), foodID, String.valueOf(buyLinePrice), customerQuantity};
          DatabaseFunctions.addData("VendorBuyLine", buyLineData);

          // Update the total price of the CustomerOrder
          String[] orderData = DatabaseFunctions.getDataFromPK("VendorOrder", String.valueOf(IDTracker.getVendorOrderID()));
          double newOrderTotal = Double.parseDouble(orderData[3]) + buyLinePrice;
          newOrderTotal = ( (double) ((int)(newOrderTotal * 100) ) ) / 100;
          DatabaseFunctions.editData("VendorOrder", String.valueOf(IDTracker.getCustomerOrderID()), "vendorOrderTotal", String.valueOf(newOrderTotal));

          // Return the sale line info to the front-end
          return buyLineData;
      }
/*
      public static void addFoodItem(String[] attributeValues) {
      	IDTracker.incFoodID();
      	String[] passedAttributes = {String.valueOf(IDTracker.getFoodID()), attributeValues[0], attributeValues[1], attributeValues[2], attributeValues[3], attributeValues[4]};
      	DatabaseFunctions.addData("FoodItems", passedAttributes);
      }
*/

      /**
       * Will get all the info about any given employee
       * @param  employeeID The ID of the employee you want to get data for
       * @return A string array of all the info of the desired employee
       */
      public static String[] getEmployeeInfo(String employeeID) {
          String[] employeeData = DatabaseFunctions.getDataFromPK("EmployeeList", employeeID);

          return employeeData;
      }

      /**
       * Will get the total cost of the current customer order
       * @return A string representation of the total cost of the current customer order
       */
      public static String getCustomerOrderTotal() {
          String[] orderInfo = DatabaseFunctions.getDataFromPK("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()));
          return orderInfo[2];
      }

      /**
       * Will get the total cost of the current vendor order
       * @return A string representation of the total cost of the current vendor order
       */
      public static String getVendorOrderTotal() {
          String[] orderInfo = DatabaseFunctions.getDataFromPK("VendorOrder", String.valueOf(IDTracker.getVendorOrderID()));
          return orderInfo[2];
      }

      /**
       * Will delete a chosen sale line from the current order
       * @param id The foodID number for the desired sale line to be deleted
       */
      public static void deleteSaleLine(String foodID) {
        // Get all the rows with the current CustomerOrderID and the foodID
        String[] attributeNames = {"CustomerOrderID", "FoodID"};
        String[] attributeValues = {String.valueOf(IDTracker.getCustomerOrderID()), foodID};
        String[][] matchingSaleLines = DatabaseFunctions.getDataFromAttributes("CustomerSaleLine", attributeNames, attributeValues);

        // DatabaseFunctions.getDataFromAttributes("CustomerOrder", )
        for (String[] saleLine : matchingSaleLines) {
            String[] currRow = DatabaseFunctions.getDataFromPK("FoodItems", saleLine[2]);
            double invFoodQuantity = Double.parseDouble(currRow[3]);
            double saleFoodQuantity = Double.parseDouble(saleLine[4]);
            double newQuantity = invFoodQuantity + saleFoodQuantity;
            newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;

            double saleTotal = Double.parseDouble(saleLine[3]);
            String[] orderInfo = DatabaseFunctions.getDataFromPK("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()));
            double newTotal = Double.parseDouble(orderInfo[2]) - saleTotal;
            newTotal = ( (double) ((int)(newTotal * 100) ) ) / 100;
            if (newTotal <= 0) {
              System.out.println("ERROR: Setting the order total to 1 cent");
              newTotal = 0.01;
            }

            System.out.println("New total: " + newTotal);

            DatabaseFunctions.editData("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()), "customerOrderTotal", String.valueOf(newTotal));


            DatabaseFunctions.editData("FoodItems", String.valueOf(saleLine[2]), "foodQuantity", String.valueOf(newQuantity));
            DatabaseFunctions.deleteData("CustomerSaleLine", saleLine[0]);
            IDTracker.decSaleLineID();
        }
      }

      /**
       * Will delete a chosen buy line from the current vendor order
       * @param foodID The ID number for the food that you want to delete from the vendor order
       */
      public static void deleteBuyLine(String foodID) {
          // Get all the rows with the current CustomerOrderID and the foodID
          String[] attributeNames = {"VendorOrderID", "FoodID"};
          String[] attributeValues = {String.valueOf(IDTracker.getVendorOrderID()), foodID};
          String[][] matchingBuyLines = DatabaseFunctions.getDataFromAttributes("VendorBuyLine", attributeNames, attributeValues);

          // DatabaseFunctions.getDataFromAttributes("CustomerOrder", )
          for (String[] buyLine : matchingBuyLines) {
              String[] currRow = DatabaseFunctions.getDataFromPK("FoodItems", buyLine[2]);
              double invFoodQuantity = Double.parseDouble(currRow[3]);
              double buyFoodQuantity = Double.parseDouble(buyLine[4]);
              double newQuantity = invFoodQuantity - buyFoodQuantity;

              newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;

              DatabaseFunctions.editData("FoodItems", String.valueOf(buyFoodQuantity), "foodQuantity", String.valueOf(newQuantity));
              DatabaseFunctions.deleteData("VendorBuyLine", buyLine[0]);
              IDTracker.decBuyLineID();
          }
      }

      /**
       * Will finalize all the current customer order information
       * @param paymentMethod The payment method the customer chose to use to pay for the order
       * @param employeeID The ID number of the employee who handled the customer order
       */
      public static void completeOrder(String paymentMethod, String employeeID) {
          // Finalize the order information
          // Change payment method
          DatabaseFunctions.editData("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()), "paymentMethod", paymentMethod);

          // Increment currCustomerOrderID to prepare for the next order
          IDTracker.incCustomerOrderID();

          // Create new CustomerOrderID
          String date = getDate();
          String[] newOrderAttributes = {String.valueOf(IDTracker.getCustomerOrderID()), date, "0.01", "cash", employeeID};
          DatabaseFunctions.addData("CustomerOrder", newOrderAttributes);
      }

      /**
       * Will finalize all the current vendor order information
       * @param employeeID The ID number of the employee who is placing the vendor order
       */
      public static void completeVendorOrder(String employeeID) {
          // Finalize the order information
          // Change payment method
          DatabaseFunctions.editData("VendorOrder", String.valueOf(IDTracker.getVendorOrderID()), "vendorName", "Bubba Gump");

          // Increment currCustomerOrderID to prepare for the next order
          IDTracker.incVendorOrderID();

          // Create new CustomerOrderID
          String date = getDate();
          String[] newOrderAttributes = {String.valueOf(IDTracker.getVendorOrderID()), "Bubba Gump", date, "0.01", employeeID};
          DatabaseFunctions.addData("CustomerOrder", newOrderAttributes);
      }

      /**
       * Cancels the current customer order by delete all of its sale lines and clearing all information about it
       * @param tableModel The tableModel holds all the information about the information about the current order, which
       * will be used to re-add all the food quantities to inventory
       */
       public static void cancelOrder(DefaultTableModel tableModel) {
           String orderID = String.valueOf(IDTracker.getCustomerOrderID());
           String nameOfTable = "FoodItems";
           String[] foodRow;
           String numSaleLinesToDelete;
           int tableSize = tableModel.getRowCount();
           int idOfFood;
           int orderToDelete = IDTracker.getCustomerOrderID();
           Double newQuantity;
           Double oldQuantity;
           Double orderQuantity;

           for (int i = 0; i < tableSize; i++) {
               idOfFood = Integer.parseInt(String.valueOf(tableModel.getValueAt(i, 0)));
               orderQuantity = Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 3)));
               foodRow = DatabaseFunctions.getDataFromPK(nameOfTable, String.valueOf(idOfFood));
               oldQuantity = Double.parseDouble(foodRow[3]);
               newQuantity = oldQuantity + orderQuantity;
               newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;

               DatabaseFunctions.editData("FoodItems", String.valueOf(idOfFood), "foodQuantity", String.valueOf(newQuantity));
           }

           numSaleLinesToDelete = DatabaseFunctions.countRows("CustomerSaleLine", "customerOrderID", String.valueOf(orderToDelete));
           DatabaseFunctions.deleteMultipleRows("CustomerSaleLine", "CustomerOrderID", String.valueOf(orderToDelete));

           for (int i = 0; i < Integer.parseInt(numSaleLinesToDelete); i++) {
               IDTracker.decSaleLineID();
           }

           DatabaseFunctions.editData("CustomerOrder", String.valueOf(orderToDelete), "customerOrderTotal", "0.01");
       }

       /**
        * Cancels the current vendor order by deleeing all of its buy lines and clearing all the information about it
        * @param tableModel The tableModel holds all the information about the information about the current vendor order, which
        * will be used to delete all the food quantities from inventory
        */
       public static void cancelVendorOrder(DefaultTableModel tableModel) {
           String orderID = String.valueOf(IDTracker.getVendorOrderID());
           String nameOfTable = "FoodItems";
           String[] foodRow;
           String numSaleLinesToDelete;
           int tableSize = tableModel.getRowCount();
           int idOfFood;
           int orderToDelete = IDTracker.getVendorOrderID();
           Double newQuantity;
           Double oldQuantity;
           Double orderQuantity;

           for (int i = 0; i < tableSize; i++) {
               idOfFood = Integer.parseInt(String.valueOf(tableModel.getValueAt(i, 0)));
               orderQuantity = Double.parseDouble(String.valueOf(tableModel.getValueAt(i, 3)));
               foodRow = DatabaseFunctions.getDataFromPK(nameOfTable, String.valueOf(idOfFood));
               oldQuantity = Double.parseDouble(foodRow[3]);
               newQuantity = oldQuantity + orderQuantity;
               newQuantity = ( (double) ((int)(newQuantity * 100) ) ) / 100;

               DatabaseFunctions.editData("FoodItems", String.valueOf(idOfFood), "foodQuantity", String.valueOf(newQuantity));
           }

           numSaleLinesToDelete = DatabaseFunctions.countRows("VendorBuyLine", "vendorOrderID", String.valueOf(orderToDelete));
           DatabaseFunctions.deleteMultipleRows("VendorBuyLine", "vendorOrderID", String.valueOf(orderToDelete));

           for (int i = 0; i < Integer.parseInt(numSaleLinesToDelete); i++) {
               IDTracker.decSaleLineID();
           }

           DatabaseFunctions.editData("VendorOrder", String.valueOf(orderToDelete), "vendorOrderTotal", "0.01");
       }

       /**
        * A function that runs throught the receipt table and deletes the first iteration of the foodID being deleted.
        * @param  tableSize                 The size of the table holding the order inforation on the GUI
        * @param  cancelledID               The foodID being deleted
        * @param  tableModel                The table that holds the order information
        * @return             A boolean value returned to tell the GUI whether it should run the function again.
        */
       public static boolean deleteRowFromReceipt(int[] tableSize, String[] cancelledID, DefaultTableModel tableModel) {
           tableSize[0] = tableModel.getRowCount();
           //System.out.println("Attempting to delete row with ID '" + cancelledID[0] + "'");
           for (int i = 0; i < tableSize[0]; i++) {
               //System.out.println("Examining row with ID '" + tableModel.getValueAt(i, 0) + "'");
               if (cancelledID[0].equals(tableModel.getValueAt(i, 0))) {
                   tableModel.removeRow(i);
                   //System.out.println("Removed item ID " + cancelledID[0]);
                   return false;
               }
           }
           return true;
       }

       /**
        * Will get a list of all individual food sales within the range of dates given
        * @param  startDate The first day to start indluding orders, inclusive
        * @param  endDate The first day to end indluding orders, inclusive
        * @return A string array of each item's value sold, where result[1] == foodID[1] and so on
        */
      // public static String[] getSalesReport(String startDate, String endDate) {
      //     String startOrderID = DatabaseFunctions.getStartOrderIDFromDates(startDate);
      //     String endOrderID = DatabaseFunctions.getEndOrderIDFromDates(endDate);
      //
      //     String[] totalSold = new String[IDTracker.getFoodID()];
      //     Arrays.fill(totalSold, "0");
      //     for (int currOrderID = Integer.parseInt(startOrderID); currOrderID < Integer.parseInt(endOrderID); currOrderID++) {
      //         try {
      //             //System.out.println("Reading order " + String.valueOf(currOrderID));
      //             // Get all sale lines for a given ID
      //             String[] attributeNames = {"CustomerOrderId"};
      //             String[] attributeValues = {String.valueOf(currOrderID)};
      //             String[][] allSaleLines = DatabaseFunctions.getDataFromAttributes("CustomerSaleLine", attributeNames, attributeValues);
      //
      //             // Add the price sold to the corresponding totalSold location for each sale line
      //             for (String[] saleLine : allSaleLines) {
      //                 int foodID = Integer.parseInt(saleLine[2]);
      //                 double salePrice = Double.parseDouble(saleLine[3]);
      //
      //                 totalSold[foodID-1] = String.valueOf(Double.parseDouble(totalSold[foodID-1]) + salePrice);
      //             }
      //         }
      //         catch (Exception e) { continue; }
      //     }
      //
      //     return totalSold;
      // }
      //
      // public static String[] getExcessReport(String startDate, String endDate) {
      //     String startOrderID = DatabaseFunctions.getStartOrderIDFromDates(startDate);
      //     String endOrderID = DatabaseFunctions.getEndOrderIDFromDates(endDate);
      //
      //     String[] totalSold = new String[IDTracker.getFoodID()];
      //     Arrays.fill(totalSold, "0");
      //     for (int currOrderID = Integer.parseInt(startOrderID); currOrderID < Integer.parseInt(endOrderID); currOrderID++) {
      //         try {
      //             //System.out.println("Reading order " + String.valueOf(currOrderID));
      //             // Get all sale lines for a given ID
      //             String[] attributeNames = {"CustomerOrderId"};
      //             String[] attributeValues = {String.valueOf(currOrderID)};
      //             String[][] allSaleLines = DatabaseFunctions.getDataFromAttributes("CustomerSaleLine", attributeNames, attributeValues);
      //
      //             // Add the quantity to the corresponding totalSold location for each sale line
      //             for (String[] saleLine : allSaleLines) {
      //                 int foodID = Integer.parseInt(saleLine[2]);
      //                 double foodQuantity = Double.parseDouble(saleLine[4]);
      //
      //                 totalSold[foodID-1] = String.valueOf(Double.parseDouble(totalSold[foodID]) + foodQuantity);
      //             }
      //         }
      //         catch (Exception e) { continue; }
      //     }
      //
      //     String[] itemsLessThanTenPercentOfInventory = new String[IDTracker.getFoodID()];
      //     int counter = 0;
      //     Arrays.fill(itemsLessThanTenPercentOfInventory, "empty");
      //     String[] currentInventory = new String[IDTracker.getFood()];
      //     for(int i = 0; i < totalSold.length; i++) {
      //         String currentItem = DatabaseFunctions.getDataFromPK("foodItems", i + 1);
      //         double currentQuantity = currentItem[3];
      //         if((currentQuantity * .10) > Double.parseDouble(totalSold[i])) {
      //             itemsLessThanTenPercentOfInventory[counter] = currentItem[1];
      //             counter++;
      //         }
      //     }
      //
      //     return itemsLessThanTenPercentOfInventory;
      // }
      //
      // public static String[] getRestockReport(String startDate, String endDate) {
      //     String startOrderID = DatabaseFunctions.getStartOrderIDFromDates(startDate);
      //     String endOrderID = DatabaseFunctions.getEndOrderIDFromDates(endDate);
      //
      //     String[] totalSold = new String[IDTracker.getFoodID()];
      //     Arrays.fill(totalSold, "0");
      //     for (int currOrderID = Integer.parseInt(startOrderID); currOrderID < Integer.parseInt(endOrderID); currOrderID++) {
      //         try {
      //             //System.out.println("Reading order " + String.valueOf(currOrderID));
      //             // Get all sale lines for a given ID
      //             String[] attributeNames = {"CustomerOrderId"};
      //             String[] attributeValues = {String.valueOf(currOrderID)};
      //             String[][] allSaleLines = DatabaseFunctions.getDataFromAttributes("CustomerSaleLine", attributeNames, attributeValues);
      //
      //             // Add the quantity to the corresponding totalSold location for each sale line
      //             for (String[] saleLine : allSaleLines) {
      //                 int foodID = Integer.parseInt(saleLine[2]);
      //                 double foodQuantity = Double.parseDouble(saleLine[4]);
      //
      //                 totalSold[foodID-1] = String.valueOf(Double.parseDouble(totalSold[foodID]) + foodQuantity);
      //             }
      //         }
      //         catch (Exception e) { continue; }
      //     }
      //
      //     String[] itemsLessThanInventory = new String[IDTracker.getFoodID()];
      //     int counter = 0;
      //     Arrays.fill(itemsLessThanInventory, "empty");
      //     String[] currentInventory = new String[IDTracker.getFood()];
      //     for(int i = 0; i < totalSold.length; i++) {
      //         String currentItem = DatabaseFunctions.getDataFromPK("foodItems", i + 1);
      //         double currentQuantity = Double.parseDouble(currentItem[3]);
      //         if(currentQuantity < Double.parseDouble(totalSold[i])) {
      //             itemsLessThanInventory[counter] = currentItem[1];
      //             counter++;
      //         }
      //     }
      //
      //     return itemsLessThanInventory;
      // }

      /**
       * Will retrieve data about a given food item when the an item is searched in the search bar
       * @param  id The FoodID that was typed into the search bar
       * @return A String array that contains all the row information about the passed food item:
       * [foodID, foodName, unitPrice, foodQuantity, storageType, packaged]
       */
      public static String[] getFoodInfo(String foodID) {
          String[] foodData = DatabaseFunctions.getDataFromPK("FoodItems", foodID);

          return foodData;
      }

      /**
       * Will return whether the current employee has access to manager privileges
       * @return A boolean of if the employee is a manager or not
       */
      public static boolean isManager() {
          int employeeID = IDTracker.getEmployeeID();
          boolean isManager = false;

          //Check permissions for employee
          String[] employeeInfo = DatabaseFunctions.getDataFromPK("EmployeeList", String.valueOf(employeeID));
          if(employeeInfo[2].equals('t')) {
              isManager = true;
          }

          return isManager;
      }

      /**
       * Testing and debugging function. Will be deleted for the final release
       * @param args Input arguments
       */
      public static void main(String[] args) {
          //...
      }
  }
