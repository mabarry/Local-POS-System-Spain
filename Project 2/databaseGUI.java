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
    // public static DefaultTableModel inventoryTable = new DefaultTableModel();
    public static DefaultTableModel employeeTable = new DefaultTableModel();
    public static DefaultTableModel itemList = new DefaultTableModel();
    public static JButton moveToSale;
    public static JTextArea output;
    public static JTextField quantity;
    public static boolean isManagerView;
    public static final JTextField customerOrderTotal = CreateTextField(42,540,465, 30, "0.0 €");

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

    public static JTextArea CreateTextArea(int x, int y, int width, int height, String text) {
        JTextArea createdTextArea = new JTextArea(text);
        createdTextArea.setBounds(x, y, width, height);
        return createdTextArea;
    }

    /**
     * A search bar that creates a button and text space for the user to look up different food items
     * to be added to a sale line in an order. This method also adds button funcitonality for different
     * objects in the employee view.
     * @param frame  The JFrame that holds the entire GUI
     */
    public static void SearchBar(JFrame frame) {
        // Initializing button
        JButton searchButton = CreateButton(1380,35,80,40, "Search");

        // Text box that will appear when button is clicked
        final JTextField searchBar = CreateTextField(580,40,780,25, "Search for an ID");
        Font font = new Font("SansSerif", Font.PLAIN, 15);
        searchBar.setFont(font);

        // Output text field, determined by TextField input
        Font outputFont = new Font("SansSerif", Font.PLAIN, 25);
        output.setFont(outputFont);
        output.setEditable(false);

        Font quantityFont = new Font("SansSerif", Font.PLAIN, 25);
        quantity.setFont(outputFont);

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        searchBar.setBorder(blackLine);

        // Creates array to hold the search input
        final String[] searchInput = new String[1];
        final String[] searchOutput = new String[1];
        final String[] quantityInput = new String[1];

        final String[] itemReturned = new String[6];
        final String[] quantityType = new String[1];
        final String[] priceType = new String[1];

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInput[0] = searchBar.getText();
                String[] searchResult = Interact.getFoodInfo(searchInput[0]);

                for (int i = 0; i < searchResult.length; i++) {
                    itemReturned[i] = searchResult[i];
                }

                if (itemReturned[5].equals("f")) {
                    quantityType[0] = "kg";
                    priceType[0] = "/kg";
                }
                else {
                    quantityType[0] = "units";
                    priceType[0] = "/unit";
                }

                output.setText("\n" + "\n" + "\n" + "\n" + "\n" + itemReturned[1] +  " " + "\n" + "(" + itemReturned[2] + priceType[0] + ", " +
                itemReturned[3] + " " + quantityType[0] + " in Inventory)");
            }
        });

        moveToSale.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOutput[0] = output.getText();
                quantityInput[0] = quantity.getText();

                // Change row to what the database fetched
                String[] row = {itemReturned[0], itemReturned[1], itemReturned[2], quantityInput[0]};
                InsertIntoTable(frame, row, tableModel);

                // Adds to vendor order if user in manager view, adds to customer order if not
                if(isManagerView == true) {
                    Interact.addToBuyLine(itemReturned[0], quantityInput[0]);
                    customerOrderTotal.setText(" " + Interact.getVendorOrderTotal() + " €");
                }
                else {
                    Interact.addToSaleLine(itemReturned[0], quantityInput[0]);
                    customerOrderTotal.setText(" " + Interact.getCustomerOrderTotal() + " €");
                }
            }
        });

        // Adds elements to the JFrame
        frame.add(searchButton);
        frame.add(searchBar);
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
         JButton salesReport = CreateButton(42,740,100,30, "Sales");
         JButton excessReport = CreateButton(152,740,100,30, "Excess");
         JButton restockReport = CreateButton(262,740,100,30, "Restock");
         JLabel reports = new JLabel("Generate Reports");
         reports.setBounds(372,740,200,30);


         // Initializes text fields
         final JTextField IDToDelete = CreateTextField(42,660,465,30, "Enter ID of Row to Delete");
         customerOrderTotal.setEditable(false);

         cancelOrder.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 if (!isManagerView) {
                     Interact.cancelOrder(tableModel);
                     tableModel.setRowCount(0);
                     customerOrderTotal.setText("0.0 €");
                }
                else {
                    Interact.cancelVendorOrder(tableModel);
                    tableModel.setRowCount(0);
                    customerOrderTotal.setText("0.0 €"); /*!!!!!*/// Make the total order display in Manager view set to 0.0
                }
             }

         });

         // A string array to hold data on a row to be deleted.
         final String[] cancelledID = new String[1];
         final int[] tableSize = new int[1];
         final int[] indexToDelete = new int[1];

         cancelRow.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 cancelledID[0] = IDToDelete.getText();
                 boolean endLoop = false;
                 while (!endLoop) {
                     endLoop = Interact.deleteRowFromReceipt(tableSize, cancelledID, tableModel);
                 }
                 //System.out.println("loop ended");
                 //System.out.println("New order total: " + Interact.getCustomerOrderTotal());

                 if (isManagerView) {
                    Interact.deleteBuyLine(String.valueOf(cancelledID[0]));
                 } else {
                    Interact.deleteSaleLine(String.valueOf(cancelledID[0]));
                 }
                 //System.out.println("New customer order total: " + Interact.getCustomerOrderTotal());
                 customerOrderTotal.setText(" " + Interact.getCustomerOrderTotal() + " €");
             }
         });

         completeOrder.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 // Complete order and send sale line to customerOrders on button click **************Drop down menu w payment method**********
                 if(isManagerView == true) {
                   Interact.completeVendorOrder(String.valueOf(IDTracker.getEmployeeID()));
                   tableModel.setRowCount(0);
                 }
                 else {
                   Interact.completeOrder("Cash", String.valueOf(IDTracker.getEmployeeID()));
                   tableModel.setRowCount(0);
                 }
                 customerOrderTotal.setText("0.0 €");
             }
         });

         salesReport.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 String[] salesReportData = Interact.getSalesReport("2022-06-11", "2022-06-12");
                 DefaultTableModel salesReportModel = new DefaultTableModel();
                 JTable salesReportTable = new JTable(salesReportModel);
                 salesReportModel.addColumn("Item ID || Total Price");

                 for (int i = (salesReportData.length - 1); i >= 0; i--) {
                     if (Double.parseDouble(salesReportData[i]) < 0.01) {
                         continue;
                     }
                      salesReportModel.insertRow(0, new Object[] { (i+1) + ": " + salesReportData[i] });
                 }
                 JOptionPane.showMessageDialog(null, new JScrollPane(salesReportTable));
             }
         });

         excessReport.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
               String[] excessReportData = Interact.getExcessReport("2022-06-11", "2022-06-12");
               DefaultTableModel excessReportModel = new DefaultTableModel();
               JTable excessReportTable = new JTable(excessReportModel);
               excessReportModel.addColumn("Items sold less than 10%");

               for (int i = excessReportData.length - 1; i >= 0; i--) {
                   if (excessReportData[i] == "empty" && i > 0) {
                       continue;
                   }
                    excessReportModel.insertRow(0, new Object[] {excessReportData[i]});
               }
               JOptionPane.showMessageDialog(null, new JScrollPane(excessReportTable));
             }
         });

         restockReport.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
               String[] restockReportData = Interact.getRestockReport("2022-06-11", "2022-06-12");
               DefaultTableModel restockReportModel = new DefaultTableModel();
               JTable restockReportTable = new JTable(restockReportModel);
               restockReportModel.addColumn("Items sold more than in Inventory");

               for (int i = 0; i <  restockReportData.length; i++) {
                   if (restockReportData[i] == "empty" && i > 0) {
                       continue;
                   }
                    restockReportModel.insertRow(0, new Object[] {restockReportData[i]});
               }
               JOptionPane.showMessageDialog(null, new JScrollPane(restockReportTable));
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

         // Adjusts IDToDelete properties
         IDToDelete.setBackground(Color.white);
         IDToDelete.setBorder(blackLine);

         // Adds elements to the JFrame
         frame.add(completeOrder);
         frame.add(cancelOrder);
         frame.add(backPanel);
         frame.add(customerOrderTotal);
         frame.add(IDToDelete);
         frame.add(cancelRow);
         frame.add(salesReport);
         frame.add(excessReport);
         frame.add(restockReport);
         frame.add(reports);
     }

    /**
     * Default sale view of items searched and returned from the database
     * @param  frame               The frame that holds the entire GUI
     * @return       The text field that stores the output of the search bar
     */
    public static JTextArea ScreenSpace(JFrame frame) {
        // Output text field, determined by TextField input
        final JTextArea output = CreateTextArea(545,90,465,440, "");

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        // Adjust 'output' properties
        output.setBorder(blackLine);
        output.setBackground(Color.white);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);

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
        tableModel.addColumn("Quantity Selected");
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(15);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(3).setPreferredWidth(60);
        return table;
    }

    /**
     * Streamlines insertion process of data into DefaultTableModels to one line of code
     * @param frame  the JFrame that holds the entire GUI
     * @param row    The row data to insert into the table
     * @param DFT    The DefaultTableModel into which we will insert the row data
     */
    public static void InsertIntoTable(JFrame frame, String[] row, DefaultTableModel DFT) {
        DFT.insertRow(DFT.getRowCount(), row);
    }

    public static void PopulateEmployeeTable(JFrame frame) {
        for(int i = 101; i <= IDTracker.getEmployeeID(); i++) {
            String[] retrievedRow = Interact.getEmployeeInfo("" + i);
            String[] insertRow = {retrievedRow[0], retrievedRow[1], retrievedRow[2]};
            InsertIntoTable(frame, insertRow, employeeTable);
        }
    }

    /**
     * A method that adds buttons that allow the functionality of switching between an
     * employee and manager view. This method also includes the button actions for each view
     * @param frame      The JFrame that holds the entire GUI.
     * @param inventory  The table that holds information on the current stock or inventory.
     * @param employees  The table that holds the information on current employees
     */
    public static void ScreenSwitcher(JFrame frame, JTable employees, JTable items) {
        // Create different views and the buttons to switch those views
        JButton managerView = CreateButton(545, 660, 465, 105, "Switch to Manager View");
        JButton employeeView = CreateButton(1022, 660, 465, 105, "Switch to Employee View (Sales)");
        JButton addItem = CreateButton(525, 35, 50, 50, "+");

        // Label that changes based on which permission view you are in
        JLabel viewLabel = new JLabel();
        viewLabel.setBounds(10, 10, 200, 25);
        viewLabel.setText("Customer Sales");
        Font font = new Font("SansSerif", Font.PLAIN, 25);
        viewLabel.setFont(font);

        // Manager view panel to appear
        JPanel coverEmployee = new JPanel();
        JPanel coverItems = new JPanel();

        // Black border to be used by both screen panels
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        // GUI field to store employee information
        coverEmployee.setBounds(1022,90,472,440);
        coverEmployee.setBorder(blackLine);
        coverEmployee.setBackground(Color.white);
        coverEmployee.add(new JScrollPane(employees));

        // GUI field to store item names and IDs
        coverItems.setBounds(1022,90,472,440);
        coverItems.setBorder(blackLine);
        coverItems.setBackground(Color.white);
        coverItems.add(new JScrollPane(items));

        frame.add(coverItems);
        PopulateEmployeeTable(frame);

        /* When manager view is pressed, new tables will appear that the manager is able to edit and view */
        managerView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Interact.isManager()) {
                    JOptionPane.showMessageDialog(null, "Switched to manager view.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error: You do not have permission to access this view.");
                }
                isManagerView = true;
                viewLabel.setText("Vendor Orders");
                output.setText(" ");
                tableModel.setRowCount(0);
                quantity.setText(" ");
                Interact.cancelOrder(tableModel);
                frame.remove(coverItems);
                frame.add(coverEmployee);
                frame.add(addItem);
                frame.repaint();
                frame.revalidate();

            }
        });

        /* When employee view is pressed, all the managerial aspects and views of the GUI are hidden and rendered
         *  usable, effectively switching the user's permissions */
        employeeView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isManagerView = false;
                viewLabel.setText("Customer Sales");
                output.setText("");
                tableModel.setRowCount(0);
                quantity.setText(" ");
                Interact.cancelOrder(tableModel);
                frame.add(coverItems);
                frame.remove(coverEmployee);
                frame.remove(addItem);
                frame.repaint();
                frame.revalidate();
            }
        });

        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              JOptionPane option = new JOptionPane();
              DefaultTableModel addItemModel = new DefaultTableModel();
              JTable addItemTable = new JTable(addItemModel);
              addItemModel.addColumn("Item Name");
              addItemModel.addColumn("Unit Price");
              addItemModel.addColumn("Storage Type");
              addItemModel.addColumn("Is Packaged (t/f)");
              //addItemModel.addColumn("");
              addItemModel.addRow(new Object[] {" "," "," "," "});

              option.showMessageDialog(null, new JScrollPane(addItemTable));

              String text = "Press to Add Food";
              String title = "Confirm?";
              int optionType = JOptionPane.OK_CANCEL_OPTION;
              int resultOfOption = JOptionPane.showConfirmDialog(null, text, title, optionType);
              if (resultOfOption == JOptionPane.OK_OPTION) {
                  String[] itemData = { String.valueOf(addItemModel.getValueAt(0, 0)) , String.valueOf(addItemModel.getValueAt(0, 1)),
                                       String.valueOf(addItemModel.getValueAt(0, 2)), String.valueOf(addItemModel.getValueAt(0, 3)) };
                  Interact.addFoodItem(itemData);
              }
            }
        });

        // adds elements to frame
        frame.add(managerView);
        frame.add(employeeView);
        frame.add(viewLabel);
    }

    /**
     * Create table to store employee data that managers can view
     * @param  frame               The frame that holds the entire GUI
     * @return       The JTable that stores employee info
     */
    public static JTable EmployeeTable(JFrame frame) {
        JTable employees = new JTable(employeeTable);
        employees.setBounds(10,10,200,200);
        employeeTable.addColumn("Employee ID");
        employeeTable.addColumn("Employee Name");
        employeeTable.addColumn("Manager Permissions?");
        TableColumnModel columnModel = employees.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(200);
        return employees;
    }


    public static JTable ItemList(JFrame frame) {
        JTable items = new JTable(itemList);
        items.setBounds(1022,90,472,375);
        itemList.addColumn("Item ID");
        itemList.addColumn("Item Name");
        TableColumnModel columnModel = items.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(200);
        return items;
    }

    public static void initializeFoodTable(JFrame frame) {
        for(int i = 1; i <= IDTracker.getFoodID(); i++) {
            String[] retrievedRow = Interact.getFoodInfo("" + i);
            String[] insertRow = {retrievedRow[0], retrievedRow[1]};
            InsertIntoTable(frame, insertRow, itemList);
        }
    }


    /**
     * The section of the program that calls the methods to create a GUI. Global
     * variables are instantiated here and the JFrame properties are set here.
     * @param args  [description]
     */
    public static void main(String[] args) {
        // Initialize the connection to the SQL database
        dbConnection.setUpConnection();
        Interact.initializer("101");

        // Initialize a new Swing window to appear when run
        JFrame frame = new JFrame("Fruit Stand Point of Sales");

        isManagerView = false;

        // Tables being initialized
        JTable table = CreateTable(frame);
        JTable employees = EmployeeTable(frame);
        JTable items = ItemList(frame);

        initializeFoodTable(frame);

        // Global buttons and boxes initialized
        moveToSale = CreateButton(1022, 540, 465, 105, "Move to Order");
        output = ScreenSpace(frame);
        quantity = CreateTextField(545,540, 465, 105, "Enter Qty. of Item");

        // Add global buttons and boxes to sale to be used in all functions
        frame.add(moveToSale);
        frame.add(quantity);
        frame.add(output);
        frame.add(items);

        // Initialize the search bar, sales box, and system to switch between management and employee views
        SearchBar(frame);
        ReceiptBox(frame, table);
        ScreenSwitcher(frame, employees, items);

        // Adjust frame properties
        frame.setSize(1920,1080);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
