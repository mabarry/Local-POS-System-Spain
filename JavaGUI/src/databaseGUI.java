import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The main page for the POS System
public class databaseGUI {

    /* Streamlines the JButton creation process into one line of code per textField. */
    public static JButton CreateButton(int x, int y, int width, int height, String message) {
        JButton createdButton = new JButton(message);
        createdButton.setBounds(x, y, width, height);
        return createdButton;
    }

    /* Streamlines the JTextField creation process into one line of code per textField. */
    public static JTextField CreateTextField(int x, int y, int width, int height) {
        JTextField createdTextField = new JTextField();
        createdTextField.setBounds(x, y, width, height);
        return createdTextField;
    }

    public static void SearchBar(JFrame frame) {
        // Initializing button
        JButton button = CreateButton(1400,35,80,30, "Search");

        // Text box that will appear when button is clicked
        final JTextField textField = CreateTextField(720,40,660,20);

        // Output text field, determined by TextField input
        final JTextField receipt = CreateTextField(705,85,780,500);

        //Create a border
        Border blackline = BorderFactory.createLineBorder(Color.black);
        textField.setBorder(blackline);
        receipt.setBorder(blackline);

        // Creates array to hold the search input
        final String[] searchInput = new String[1];

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInput[0] = textField.getText();
            /* TODO: call database class' function to pull the value of searchInput[0] from table fooditems
            in the database set the output as the database value. Have quantity, price per kg, and name, at least.
            String quantity = SELECT __ FROM ...
            String ID = SELECT ___ FROM ...
            may need multiple output textFields depending on the variables
            */
                receipt.setText(searchInput[0]);
            }
        });

        // Adds elements to the JFrame
        frame.add(button);
        frame.add(receipt);
        frame.add(textField);

    }

    public static void ReceiptBox(JFrame frame) {
        // Initializes the background panel
        JPanel backPanel = new JPanel();

        //Create a border
        Border blackline = BorderFactory.createLineBorder(Color.black);

        // Adjusts backPanel properties
        backPanel.setBounds(40,40,620,700);
        backPanel.setBorder(blackline);
        backPanel.setBackground(Color.white);

        // Adds elements to the JFrame
        frame.add(backPanel);

    }

    public static void ScreenSpace(JFrame frame) {

    }

    public static void main(String[] args) {
        // Initialize a new Swing window to appear when run
        JFrame frame = new JFrame("Fruit Stand Point of Sales (definitely not piece of shit)");

        SearchBar(frame);
        ReceiptBox(frame);

        // TODO: Populate JTable
        JTable table = new JTable();
        table.setBounds(30,40,200,200);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Adjust frame properties
        frame.setSize(1920,1080);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}