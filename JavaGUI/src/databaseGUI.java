import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The main page for the POS System
public class databaseGUI {

    public static DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public static JButton moveToSale;
    public static JTextField output;
    public static JTextField quantity;


    /* Streamlines the JButton creation process into one line of code per textField. */
    public static JButton CreateButton(int x, int y, int width, int height, String message) {
        JButton createdButton = new JButton(message);
        createdButton.setBounds(x, y, width, height);
        return createdButton;
    }

    /* Streamlines the JTextField creation process into one line of code per textField. */
    public static JTextField CreateTextField(int x, int y, int width, int height, String text) {
        JTextField createdTextField = new JTextField(text);
        createdTextField.setBounds(x, y, width, height);
        return createdTextField;
    }

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
                InsertIntoTable(frame, row);
            }
        });

        // Adds elements to the JFrame
        frame.add(searchButton);
        frame.add(searchBar);
//        frame.remove();
    }

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

//        customerOrderTotal.setBounds(42,540,465,30);
        customerOrderTotal.setBackground(Color.white);
        customerOrderTotal.setBorder(blackLine);

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

    public static JTextField ScreenSpace(JFrame frame) {

        // Output text field, determined by TextField input
        final JTextField output = CreateTextField(545,90,945,300, "");

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);
        output.setBorder(blackLine);

        return output;
    }

    public static JTable CreateTable(JFrame frame) {
        JTable table = new JTable(tableModel);
        table.setBounds(100,40,200,200);
        tableModel.addColumn("ID");
        tableModel.addColumn("Item");
        tableModel.addColumn("Price");
        tableModel.addColumn("Quantity");
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(180);
        return table;
    }

    public static void InsertIntoTable(JFrame frame, String[] row) {
        // TODO: Populate JTable
        tableModel.insertRow(tableModel.getRowCount(), row);
    }

    public static void ScreenSwitcher(JFrame frame) {
        // creates button to change views
        JButton managerView = CreateButton(545,550, 465, 105, "Switch to Manager View");
        JButton employeeView = CreateButton(1022, 550, 465, 105, "Switch to Employee View");
        JPanel cover = new JPanel();

        //Create a border
        Border blackLine = BorderFactory.createLineBorder(Color.black);

        // Adjusts backPanel properties
        cover.setBounds(545,90,945,600);
        cover.setBorder(blackLine);
        cover.setBackground(Color.white);

        managerView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(moveToSale);
                frame.remove(output);
                frame.remove(quantity);
                frame.add(cover);
            }
        });

        employeeView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.add(moveToSale);
                frame.add(output);
                frame.add(quantity);
                frame.remove(cover);
            }
        });

        // adds elements to frame
        frame.add(managerView);
        frame.add(employeeView);

    }

    public static void main(String[] args) {
        // Initialize a new Swing window to appear when run
        JFrame frame = new JFrame("Fruit Stand Point of Sales");
        JTable table = CreateTable(frame);
        moveToSale = CreateButton(1022, 400, 465, 105, "Move to Sale");
        output = ScreenSpace(frame);
        quantity = CreateTextField(545,400,462,105, "Enter Qty. of Item");
        frame.add(moveToSale);
        frame.add(quantity);
        frame.add(output);

        // TODO: call create customer order

        SearchBar(frame);
        ReceiptBox(frame, table);
        ScreenSwitcher(frame);

        // Adjust frame properties
        frame.setSize(1920,1080);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}