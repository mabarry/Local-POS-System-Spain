import java.io.*;
import java.util.*;

public class Interact {
    public static void RAISEERROR() {
        System.out.println("error");
    }


    public static String[] itemSearchDisplay(String id) {
        String[] foodData = Backend.getData("FoodItems", id);

        return foodData;
    }


    public static String[] addToSaleLine(String foodID, String customerQuantity) {
        String[] foodData = Backend.getData("FoodItems", foodID);
        double inventoryQuantity = Double.parseDouble(foodData[3]);
        double unitPrice = Double.parseDouble(foodData[2]);

        if (Double.parseDouble(customerQuantity) > inventoryQuantity) {
            RAISEERROR();
        }

        // Assuming there is a customer order already created, 
        // Subtract quantity from inventory
        double newQuantity = inventoryQuantity - Double.parseDouble(customerQuantity);
        Backend.editData("FoodItems", foodID, "foodQuantity", String.valueOf(newQuantity));

        // Create new sale line
        double saleLinePrice = Double.parseDouble(customerQuantity) * unitPrice;
        String[] saleLineData = {String.valueOf(PKTracker.currSaleLineID), String.valueOf(PKTracker.currCustomerOrderID), foodID, String.valueOf(saleLinePrice), customerQuantity};
        Backend.addData("CustomerSaleLine", saleLineData);

        // Update the total price of the CustomerOrder
        String[] orderData = Backend.getData("CustomerOrder", String.valueOf(PKTracker.currCustomerOrderID));
        double newOrderTotal = Double.parseDouble(orderData[2]) + saleLinePrice;
        Backend.editData("CustomerOrder", String.valueOf(PKTracker.currCustomerOrderID), "customerOrderTotal", String.valueOf(newOrderTotal));

        // Return the sale line info to the front-end
        return saleLineData;
    }

    public static void deleteSaleLine() {

    }

    public static void completeOrder() {
        // ...
        // ...
        // ...
        // Create new CustomerOrder
    }




    // Use for testing
    public static void main(String[] args) {
        PKTracker.setSaleLineID(1005355);
        PKTracker.setCustomerOrderID(10991);

        addToSaleLine("13", "0.5");
    }
}