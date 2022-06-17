import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Matthew Barry
 * @author Cameron Hoholik-Carlson
 * @author Harrison Kuhn
 * @author Kyle Owens
 * This is the main class that holds the code for the GUI and POS system
 */

public class databaseGUI {
    public static DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public static DefaultTableModel inventoryTable = new DefaultTableModel();
    public static DefaultTableModel employeeTable = new DefaultTableModel();
    public static JButton moveToSale;
    public static JTextField output;
    public static JTextField quantity;

    /**
     * Streamlines the JButton creation process into one line of code per textField.
     * @param  x                     The X coordinate of the new button on the JFrame.
     * @param  y                     The Y coordinate of the new button on the JFrame.
     * @param  width                 The total width of the new button being created.
     * @param  height                The total height of the new button being created.
     * @param  message               The text that fills the space inside of the button.
     * @return                       The button object being created.
     */
    public static JButton CreateButton(int x, int y, int width, int height, String message) {
        JButton createdButton = new JButton(message);
        createdButton.setBounds(x, y, width, height);
        return createdButton;
    }

    /**
     * Streamlines JTextField creation process by setting the size and text of the field.
     * @param  x                    The X coordinate of the new button on the JFrame.
     * @param  y                    The Y coordinate of the new button on the JFrame.
     * @param  width                The total width of the new button being created.
     * @param  height               The total height of the new button being created.
     * @param  text                 The text that fills the space inside of the text field.
     * @return                      The text field being created
     */
    public static JTextField CreateTextField(int x, int y, int width, int height, String text) {
        JTextField createdTextField = new JTextField(text);
        createdTextField.setBounds(x, y, width, height);
        return createdTextField;
    }

    /**
     * A search bar that creates a button and text space for the user to look up different food items
     * to be added to a sale line in an order. This method also adds button funcitonality for different
     * objects in the employee view.
     * @param frame  The JFrame that holds the entire GUI
     */
    public static void SearchBar(JFrame frame) {
        // Initializing button
        JButton searchButton = CreateButton(1400,35,80,40, "Search");

        // Text box that will appear when button is clicked
        final JTextField searchBar = CreateTextField(720,40,660,25, "Search for an ID");
        Font font = new Font("SansSerif", Font.PLAIN, 15);
        searchBar.setFont(font);

        // Output text field, determined by TextField input
        Font outputFont = new Font("SansSerif", Font.PLAIN, 25);
        output.setFont(outputFont);
        output.setEditable(false);

//        final JTextField quantity = CreateTextField(545,400,462,105, "Enter Qty. of Item");
        Font quantityFont = new Font("SansSerif", Font.PLAIN, 25);
        quantity.setFont(outputFont);

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        searchBar.setBorder(blackLine);

        // Creates array to hold the search input
        final String[] searchInput = new String[1];
        final String[] searchOutput = new String[1];
        final String[] quantityInput = new String[1];

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInput[0] = searchBar.getText();
                /* TODO: call database class' function to pull the value of searchInput[0] from table fooditems
                in the database set the output as the database value. Have quantity, price per kg, and name, at least.
                String quantity = SELECT __ FROM ...
                String ID = SELECT ___ FROM ...
                may need multiple output textFields depending on the variables
                */
                output.setText(searchInput[0]);
            }
        });

        moveToSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOutput[0] = output.getText();
                quantityInput[0] = quantity.getText();
                // Change row to what the database fetched
                String[] row = {"n",searchInput[0],"$$$$",quantityInput[0]};
                InsertIntoTable(frame, row, tableModel);
            }
        });

        // Adds elements to the JFrame
        frame.add(searchButton);
        frame.add(searchBar);
//        frame.remove();
    }

    /**
     * A method that creates an area to handle orders and its corresponding buttons
     * @param frame  The JFrame that holds the entire GUI
     * @param table  The table that holds sale lines and order information to be editted
     */
    public static void ReceiptBox(JFrame frame, JTable table) {
        // Initializes the background panel and buttons
        JPanel backPanel = new JPanel();
        JButton completeOrder = CreateButton(42,50,465,30, "Complete Order");
        JButton cancelOrder = CreateButton(42,620,465,30, "Cancel Order");
        JButton cancelRow = CreateButton(42,700,465,30, "Cancel Line");

        // Initializes text fields
        final JTextField rowToDelete = CreateTextField(42,660,465,30, "Enter ID of Row to Delete");
        final JTextField customerOrderTotal = CreateTextField(42,540,465, 30, "Total");
        customerOrderTotal.setEditable(false);

        cancelOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setRowCount(0);
            }
        });

        // A string array to hold data on a row to be deleted.
        final String[] cancelledRow = new String[1];

        cancelRow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelledRow[0] = rowToDelete.getText();
                tableModel.removeRow(Integer.parseInt(cancelledRow[0]));
            }
        });

        completeOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // all backend sshtuf
            }
        });

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        // Adjusts backPanel properties
        backPanel.setBounds(40,90,468,440);
        backPanel.setBorder(blackLine);
        backPanel.setBackground(Color.white);
        backPanel.add(new JScrollPane(table));

        // Adjusts customerOrderTotal properties
        customerOrderTotal.setBackground(Color.white);
        customerOrderTotal.setBorder(blackLine);

        // Adjusts rowToDelete properties
        rowToDelete.setBackground(Color.white);
        rowToDelete.setBorder(blackLine);

        // Adds elements to the JFrame
        frame.add(completeOrder);
        frame.add(cancelOrder);
        frame.add(backPanel);
        frame.add(customerOrderTotal);
        frame.add(rowToDelete);
        frame.add(cancelRow);
    }

    /**
     * Default sale view of items searched and returned from the database
     * @param  frame               The frame that holds the entire GUI
     * @return       The text field that stores the output of the search bar
     */
    public static JTextField ScreenSpace(JFrame frame) {
        // Output text field, determined by TextField input
        final JTextField output = CreateTextField(545,90,945,300, "");

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        output.setBorder(blackLine);

        return output;
    }

    /**
     * Creates the sale table to be used during checkout
     * @param  frame               The fram that holds the entire GUI
     * @return       JTable that is created with the given fields/columns.
     */
    public static JTable CreateTable(JFrame frame) {
        JTable table = new JTable(tableModel);
        table.setBounds(100,40,200,200);
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Unit Price");
        tableModel.addColumn("Amnt in Inventory");
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(180);
        return table;
    }

    /**
     * Streamlines insertion process of data into DefaultTableModels to one line of code
     * @param frame  the JFrame that holds the entire GUI
     * @param row    The row data to insert into the table
     * @param DFT    The DefaultTableModel into which we will insert the row data
     */
    public static void InsertIntoTable(JFrame frame, String[] row, DefaultTableModel DFT) {
        // TODO: Populate JTable
        DFT.insertRow(DFT.getRowCount(), row);
    }

    /**
     * A method that adds buttons that allow the functionality of switching between an
     * employee and manager view. This method also includes the button actions for each view
     * @param frame      The JFrame that holds the entire GUI.
     * @param inventory  The table that holds information on the current stock or inventory.
     * @param employees  The table that holds the information on current employees
     */
    public static void ScreenSwitcher(JFrame frame, JTable inventory, JTable employees) {
        // Create different views and the buttons to switch those views
        JButton managerView = CreateButton(545,550, 465, 105, "Switch to Manager View");
        JButton employeeView = CreateButton(1022, 550, 465, 105, "Switch to Employee View (Sales)");
        JButton updateItems = CreateButton(545, 660, 465, 105, "Update Inventory Item(s)");
        JPanel coverInventory = new JPanel();
        JPanel coverEmployee = new JPanel();

        // Black border to be used by both screen banels
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        // GUI field to store inventory information
        coverInventory.setBounds(545,90,472,450);
        coverInventory.setBorder(blackLine);
        coverInventory.setBackground(Color.white);
        coverInventory.add(new JScrollPane(inventory));

        // GUI field to store employee information
        coverEmployee.setBounds(1022,90,472,450);
        coverEmployee.setBorder(blackLine);
        coverEmployee.setBackground(Color.white);
        coverEmployee.add(new JScrollPane(employees));

        /* When manager view is pressed, new tables will appear that the manager is able to edit and view */
        managerView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(moveToSale);
                frame.remove(output);
                frame.remove(quantity);
                frame.add(updateItems);
                frame.add(coverInventory);
                frame.add(coverEmployee);
                frame.repaint();
                frame.revalidate();
            }
        });

        /* When employee view is pressed, all the managerial aspects and views of the GUI are hidden and rendered
         *  usable, effectively switching the user's permissions */
        employeeView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.add(moveToSale);
                frame.add(output);
                frame.add(quantity);
                frame.remove(updateItems);
                frame.remove(coverInventory);
                frame.remove(coverEmployee);
                frame.repaint();
            }
        });

        /* When updateItems is pressed, the inventory in our database will be updated to hold the new values
         *  input by the manager */
        updateItems.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: Update food items
                // loop getValueAt(row, col) until whole list is updated
            }
        });

        // adds elements to frame
        frame.add(managerView);
        frame.add(employeeView);
    }

    /**
     * Create table to store food and item data from inventory that managers can view and edit.
     * @param  frame               The frame that holds the entire GUI
     * @return       The JTable storing the information for the food items in the database inventory
     */
    public static JTable InventoryTable(JFrame frame) {
        JTable inventory = new JTable(inventoryTable);
        inventory.setBounds(10,10,200,200);
        inventoryTable.addColumn("ID");
        inventoryTable.addColumn("Edit Item");
        inventoryTable.addColumn("Edit Unit Price");
        inventoryTable.addColumn("Edit Amnt in Stock");
        TableColumnModel columnModel = inventory.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(180);
        String[] row = {"n", "example item" ,"$$$$", "example quantity"}; // EXAMPLE //
        InsertIntoTable(frame, row, inventoryTable);
        return inventory;
    }

    /**
     * Create table to store employee data that managers can view
     * @param  frame               The frame that holds the entire GUI
     * @return       The JTable that stores employee info
     */
    public static JTable EmployeeTable(JFrame frame) {
        JTable employees = new JTable(employeeTable);
        employees.setBounds(10,10,200,200);
        employeeTable.addColumn("Employee Name");
        employeeTable.addColumn("Employee ID");
        employeeTable.addColumn("Manager Permissions?");
        TableColumnModel columnModel = employees.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        return employees;
    }

    /**
     * The section of the program that calls the methods to create a GUI. Global
     * variables are instantiated here and the JFrame properties are set here.
     * @param args  [description]
     */
    public static void main(String[] args) {


        // Initialize a new Swing window to appear when run
        JFrame frame = new JFrame("Fruit Stand Point of Sales");

        // Tables being initialized
        JTable table = CreateTable(frame);
        JTable inventory = InventoryTable(frame);
        JTable employees = EmployeeTable(frame);

        // Global buttons and boxes initialized
        moveToSale = CreateButton(1022, 400, 465, 105, "Move to Sale");
        output = ScreenSpace(frame);
        quantity = CreateTextField(545,400,462,105, "Enter Qty. of Item");

        // Add global buttons and boxes to sale to be used in all functions
        frame.add(moveToSale);
        frame.add(quantity);
        frame.add(output);

        // TODO: call create customer order

        // Initialize the search bar, sales box, and system to switch between management and employee views
        SearchBar(frame);
        ReceiptBox(frame, table);
        ScreenSwitcher(frame, inventory, employees);

        // Adjust frame properties
        frame.setSize(1920,1080);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}