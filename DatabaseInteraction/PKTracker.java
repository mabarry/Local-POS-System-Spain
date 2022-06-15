/**
 * Class to keep track of all the current ID numbers in our database, and give functions in order to change them
 * Created so all classes can have access to the most up to date ID values
 * Can use the IDs to properly insert data into the DB
 * @author Matthew Barry
 * @author Cameron Hoholik-Carlson
 * @author Harrison Kuhn
 * @author Kyle Owens
 */
public class PKTracker {
    static int currFoodID;
    static int currEmployeeID;
    static int currVendorOrderID;
    static int currCustomerOrderID;
    static int currBuyLineID;    // TODO: delete vendor (tables) and change vendorLineID to buyLineID
    static int currSaleLineID;

    /**
     * Set all the primary key ID values to their starting values
     */
    public static void setToDefaults() {
        currFoodID = 1;
        currEmployeeID = 101;
        currVendorOrderID = 1001;
        currCustomerOrderID = 10001;
        currSaleLineID = 100001;
        currBuyLineID = 1000001;
    }

    /**
     * Set all of the primary key IDs to new values all at once
     * @param foodID  The new foodID
     * @param empID   The new employeeID
     * @param venID   The new vendorOrderID
     * @param cusID   The new customerOrderID
     * @param saleID  The new customerSaleLineID
     * @param buyID   The new vendorBuyLineID
     */
    public static void setAllIDs(int foodID, int empID, int venID, int cusID, int saleID, int buyID) {
        currFoodID = foodID;
        currEmployeeID = empID;
        currVendorOrderID = venID;
        currCustomerOrderID = cusID;
        currSaleLineID = saleID;
        currBuyLineID = buyID;
    }

    /**
     * Set the new foodID
     * @param id  The new foodID
     */
    public static void setFoodID(int id) { currFoodID = id; }

    /**
     * Set the new employeeID
     * @param id  The new employeeID
     */
    public static void setEmployeeID(int id) { currEmployeeID = id; }

    /**
     * Set the new vendorOrderID
     * @param id  The new vendorOrderID
     */
    public static void setVendorOrderID(int id) { currVendorOrderID = id; }

    /**
     * Set the new customerOrderID
     * @param id  The new customerOrderID
     */
    public static void setCustomerOrderID(int id) { currCustomerOrderID = id; }

    /**
     * Set the new customerSaleLineID
     * @param id  The new customerSaleLineID
     */
    public static void setSaleLineID(int id) { currSaleLineID = id; }

    /**
     * Set the new vendorBuyLineID
     * @param id  The new vendorBuyLineID
     */
    public static void setBuyLineID(int id) { currBuyLineID = id; }


    /**
     * Inrement the foodID
     */
    public static void incFoodID() { currFoodID++; }

    /**
     * Increment the employeeID
     */
    public static void incEmployeeID() { currEmployeeID++; }

    /**
     * Increment the vendorOrderID
     */
    public static void incVendorOrderID() { currVendorOrderID++; }

    /**
     * Increment the customerOrderID
     */
    public static void incCustomerOrderID() { currCustomerOrderID++; }

    /**
     * Inrement the customerSaleLineID
     */
    public static void incSaleLineID() { currSaleLineID++; }

    /**
     * Increment the vendorBuyLineID
     */
    public static void incBuyLineID() { currBuyLineID++; }



    /**
     * Decrement the foodID
     */
    public static void decFoodID() { currFoodID--; }

    /**
     * Decrement the employeeID
     */
    public static void decEmployeeID() { currEmployeeID--; }

    /**
     * Decrement the vendorOrderID
     */
    public static void decVendorOrderID() { currVendorOrderID--; }

    /**
     * Decrement the customerOrderID
     */
    public static void decCustomerOrderID() { currCustomerOrderID--; }
    public static void decSaleLineID() { currSaleLineID--; }
    public static void decBuyLineID() { currBuyLineID--; }

    public static String getFoodID() { return currFoodID; }
    public static String getEmployeeID() { return currEmployeeID; }
    public static String getVendorOrderID() { return currVendorOrderID; }
    public static String getCutomerOrderID() { return currCustomerOrderID; }
    public static String getSaleLineID() { return currSaleLineID; }
    public static String getBuyLineID() { return currBuyLineID; }
}
