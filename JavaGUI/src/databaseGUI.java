import javax.swing.*;
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

     public static void main(String[] args) {
         // Initialize a new Swing window to appear when run
         JFrame frame = new JFrame();

         // Adjust frame properties
         frame.setSize(1920,1080);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         // add panel to frame
         JPanel panel = new JPanel();
         panel.setBounds(20,20,620,750);
         panel.setBackground(Color.lightGray);

         // TODO: Populate JTable
         JTable table = new JTable();
         table.setBounds(30,40,200,200);
         JScrollPane scrollPane = new JScrollPane(table);
         frame.add(scrollPane);

         // Text box that will appear when button is clicked
         final JTextField textField = CreateTextField(700,30,660,20);
         final String[] searchInput = new String[1];

         // Initializing button
         JButton button = CreateButton(1380,25,80,30, "Search");

         // Output text field, determined by TextField input
         final JTextField output = CreateTextField(685,85,800,500);

         button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 searchInput[0] = textField.getText();
                 /* TODO: call database class' function to pull the value of searchInput[0] from table fooditems in the database
                     set the output as the database value. Have quantity, price per kg, and name, at least.

                    String quantity = SELECT __ FROM ...
                    String ID = SELECT ___ FROM ...

                    may need multiple output textFields depending on the variables
                 */
                 output.setText(searchInput[0]);
             }
         });

         // Add all created fields to be visible in the panel
         frame.add(textField);
         frame.add(button);
         frame.add(output);

         // Add panel to the frame, make visible
         frame.add(panel);
         frame.setLayout(null);
         frame.setVisible(true);
     }
}
