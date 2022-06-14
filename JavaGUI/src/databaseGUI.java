import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The main page for the POS System
public class databaseGUI {

     public static JButton CreateButton(int x, int y, int width, int height, String message) {
         JButton createdButton = new JButton(message);
         createdButton.setBounds(x, y, width, height);
         return createdButton;
     }

    public static JTextField CreateTextField(int x, int y, int width, int height) {
        JTextField createdTextField = new JTextField();
        createdTextField.setBounds(x, y, width, height);
        return createdTextField;
    }

     public static void main(String[] args) {
         // Initialize a new Swing window to appear when run
         JFrame frame = new JFrame();

         // Text box that will appear when button is clicked
         final JTextField textField = CreateTextField(450,75,660,20);
         final String[] searchInput = new String[1];

         // Initializing button
         JButton button = CreateButton(1130,70,80,30, "Search");

         // Output text field, determined by TextField input
         final JTextField output = CreateTextField(1130,275,80,30);

         button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                searchInput[0] = textField.getText();
                output.setText(searchInput[0]);
             }
         });

         frame.add(button);
         frame.add(output);
         frame.add(textField);

         frame.setSize(1280,720);
         frame.setLayout(null);
         frame.setVisible(true);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     }
}
