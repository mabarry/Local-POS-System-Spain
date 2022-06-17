/**
 * Class to keep track of all the current ID numbers in our database, and give functions in order to change them
 * Created so all classes can have access to the most up to date ID values
 * Can use the IDs to properly insert data into the DB
 * @author Matthew Barry
 * @author Cameron Hoholik-Carlson
 * @author Harrison Kuhn
 * @author Kyle Owens
 */
public class IDTracker {
    private static int currFoodID;
    private static int currEmployeeID;
    private static int currVendorOrderID;
    private static int currCustomerOrderID;
    private static int currBuyLineID;    // TODO: delete vendor (tables) and change vendorLineID to buyLineID
    private static int currSaleLineID;

    /**
     * Set all the primary key ID values to their starting values
     */
    public static void setToDefaults() {
        currFoodID = 1;
        currEmployeeID = 101;
        currVendorOrderID = 1001;
        currCustomerOrderID = 10001;
        currBuyLineID = 100001;
        currSaleLineID = 1000001;
    }

    /**
     * Set all of the primary key IDs to new values all at once
     * @param foodID  The new foodID
     * @param empID   The new employeeID
     * @param venID   The new vendorOrderID
     * @param cusID   The new customerOrderID
     * @param buyID   The new vendorBuyLineID
     * @param saleID  The new customerSaleLineID
     */
    public static void setAllIDs(int foodID, int empID, int venID, int cusID, int buyID, int saleID) {
        setToDefaults();

        currFoodID = foodID;
        currEmployeeID = empID;
        currVendorOrderID = venID;
        currCustomerOrderID = cusID;
        currBuyLineID = buyID;
        currSaleLineID = saleID;

        //System.out.println("Initial IDs = [" + currFoodID + ", " + currEmployeeID + ", " + currVendorOrderID + ", " + currCustomerOrderID + ", " + currBuyLineID + ", " + currSaleLineID + "]");
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
     * Set the new vendorBuyLineID
     * @param id  The new vendorBuyLineID
     */
    public static void setBuyLineID(int id) { currBuyLineID = id; }

    /**
     * Set the new customerSaleLineID
     * @param id  The new customerSaleLineID
     */
    public static void setSaleLineID(int id) { currSaleLineID = id; }





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
     * Increment the vendorBuyLineID
     */
    public static void incBuyLineID() { currBuyLineID++; }

    /**
     * Inrement the customerSaleLineID
     */
    public static void incSaleLineID() { currSaleLineID++; }





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

    /**
     * Decrement the buyLineID
     */
    public static void decBuyLineID() { currBuyLineID--; }

    /**
     * Decrement the saleLineID
     */
    public static void decSaleLineID() { currSaleLineID--; }





    /**
     * Get the next available foodID
     * @return The next foodID
     */
    public static int getFoodID() { return currFoodID; }

    /**
     * Get the next available employeeID
     * @return The next employeeID
     */
    public static int getEmployeeID() { return currEmployeeID; }

    /**
     * Get the next available vendorOrderID
     * @return The next available vendorOrderID
     */
    public static int getVendorOrderID() { return currVendorOrderID; }

    /**
     * Get the next available customerOrderID
     * @return The next available customerOrderID
     */
    public static int getCustomerOrderID() { return currCustomerOrderID; }

    /**
     * Get the next available buyLineID
     * @return The next available buyLineID
     */
    public static int getBuyLineID() { return currBuyLineID; }

    /**
     * Get the next available saleLineID
     * @return The next available saleLineID
     */
    public static int getSaleLineID() { return currSaleLineID; }
}
