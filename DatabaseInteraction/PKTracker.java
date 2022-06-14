import java.io.*;
import java.util.*;


/*
Class to keep track of all the current ID numbers in our database, and give functions in order to change them
Created so all classes can have access to the most up to date ID values
Can use the IDs to properly insert data into the DB
*/
public class PKTracker {
    static int currFoodID;
    static int currEmployeeID;
    static int currVendorOrderID;
    static int currCustomerOrderID;
    static int currBuyLineID;    // TODO: nuke vendor (tables) and change vendorLineID to buyLineID
    static int currSaleLineID;
    

    public static void setToDefaults() {
        currFoodID = 1;
        currEmployeeID = 101;
        currVendorOrderID = 1001;
        currCustomerOrderID = 10001;
        currSaleLineID = 100001;
        currBuyLineID = 1000001;
    }


    public static void setAllIDs(int foodID, int empID, int venID, int cusID, int saleID, int buyID) {
        currFoodID = foodID;
        currEmployeeID = empID;
        currVendorOrderID = venID;
        currCustomerOrderID = cusID;
        currSaleLineID = saleID;
        currBuyLineID = buyID;
    }


    public static void setFoodID(int id) { currFoodID = id; }
    public static void setEmployeeID(int id) { currEmployeeID = id; }
    public static void setVendorOrderID(int id) { currVendorOrderID = id; }
    public static void setCustomerOrderID(int id) { currCustomerOrderID = id; }
    public static void setSaleLineID(int id) { currSaleLineID = id; }
    public static void setBuyLineID(int id) { currBuyLineID = id; }

    public static void incFoodID() { currFoodID++; }
    public static void incEmployeeID() { currEmployeeID++; }
    public static void incVendorOrderID() { currVendorOrderID++; }
    public static void incCustomerOrderID() { currCustomerOrderID++; }
    public static void incSaleLineID() { currSaleLineID++; }
    public static void incBuyLineID() { currBuyLineID++; }

    public static void decFoodID() { currFoodID--; }
    public static void decEmployeeID() { currEmployeeID--; }
    public static void decVendorOrderID() { currVendorOrderID--; }
    public static void decCustomerOrderID() { currCustomerOrderID--; }
    public static void decSaleLineID() { currSaleLineID--; }
    public static void decBuyLineID() { currBuyLineID--; }
}