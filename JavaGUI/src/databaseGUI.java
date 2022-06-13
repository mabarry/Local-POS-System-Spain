import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// The main page for the POS System
public class databaseGUI {

     public static void main(String[] args) {
         // Initialize a new Swing window to appear when run
         JFrame frame = new JFrame();

         // Text box that will appear when button is clicked
         final JTextField textField = new JTextField();
         textField.setBounds(50,50,150,20);

         // Initializing button
         JButton button = new JButton("Click");
         button.setBounds(130,100,100,40);
         button.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 textField.setText("The button was clicked!");
             }
         });

         frame.add(button);
         frame.add(textField);

         frame.setSize(1280,720);
         frame.setLayout(null);
         frame.setVisible(true);
     }
}
