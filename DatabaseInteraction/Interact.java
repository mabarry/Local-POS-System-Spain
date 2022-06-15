
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
        DatabaseFunctions.editData("FoodItems", foodID, "foodQuantity", String.valueOf(newQuantity));

        // Create a new sale line and add it to the database
        double saleLinePrice = Double.parseDouble(customerQuantity) * unitPrice;
        String[] saleLineData = {String.valueOf(IDTracker.getSaleLineID()), String.valueOf(IDTracker.getCustomerOrderID()), foodID, String.valueOf(saleLinePrice), customerQuantity};
        DatabaseFunctions.addData("CustomerSaleLine", saleLineData);
        IDTracker.incSaleLineID();

        // Update the total price of the CustomerOrder
        String[] orderData = DatabaseFunctions.getDataFromPK("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()));
        double newOrderTotal = Double.parseDouble(orderData[2]) + saleLinePrice;
        DatabaseFunctions.editData("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()), "customerOrderTotal", String.valueOf(newOrderTotal));

        // Return the sale line info to the front-end
        return saleLineData;
    }

    /**
     * Will delete a chosen sale line from the current order
     * @param id The foodID number for the desired sale line to be deleted
     */
    public static void deleteSaleLine(String foodID) {
        DatabaseFunctions.deleteData("CustomerSaleLine", foodID);

        // Get all the rows with the current CustomerOrderID and the foodID
        // DatabaseFunctions.getDataFromAttributes("CustomerOrder", )
    }

    /**
     * Will finalize the order information in the database and create a new, empty order
     */
    public static void completeOrder(String paymentMethod, String employeeID) {
        // Finalize the order information
        // Change payment method
        DatabaseFunctions.editData("CustomerOrder", String.valueOf(IDTracker.getCustomerOrderID()), "paymentMethod", paymentMethod);

        // Increment currCustomerOrderID to prepare for the next order
        IDTracker.incCustomerOrderID();

        // Create new CustomerOrderID
        String date = new java.util.Date().toString();
        String[] newOrderAttributes = {String.valueOf(IDTracker.getCustomerOrderID()), date, "0.01", "Null", employeeID};
        DatabaseFunctions.addData("CustomerOrder", newOrderAttributes);
    }

    /**
     * Cancels the current customer order. Will delete all the sale lines and reset all the order details
     */
    public static void cancelOrder() {
        String orderID = String.valueOf(IDTracker.getCustomerOrderID());

        // Delete all sale lines
        // Decrement the saleLineID each time
        //
    }

    /**
     * Will query the database to display information about the shop that only the manager has permission to see
     */
    public static void switchToManager() {
        //...
    }

    /**
     * Testing and debugging function. Will be deleted for the final release
     * @param args Input arguments
     */
    public static void main(String[] args) {
        IDTracker.setSaleLineID(1005355);
        IDTracker.setCustomerOrderID(10991);

        addToSaleLine("13", "0.5");
    }
}
