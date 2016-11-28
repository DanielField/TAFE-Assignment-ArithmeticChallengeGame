package arithmeticchallengegame.GUI;

import arithmeticchallengegame.Log;
import arithmeticchallengegame.Main;
import arithmeticchallengegame.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 *
 * @author Daniel Field
 */
public class MainMenu extends JPanel implements ActionListener {

    // <editor-fold desc="Variables">
    // Form
    JLabel lblPort, lblHost;
    JTextField txtHost;
    JSpinner spnPortNo;
    //

    // Button variables
    JButton btnHost, btnJoin, btnExit;
    int btnHeight = 30, btnWidth = 80;
    //

    Font font = Font.decode("Arial");

    static JFrame frmMain;

    // </editor-fold>
    // <editor-fold desc="Init">
    
    public MainMenu() {
        setLayout(null);

        initialiseComponents();
        Utilities.addComponents(this, lblPort, lblHost, txtHost, spnPortNo, btnHost, btnJoin, btnExit);
    }

    /**
     * This method is to define and draw the components on the frame. This has
     * been done to clean up the constructor.
     *
     * Return true if the method runs successfully. This is done in case it is
     * necessary to know whether the components were successfully initialised
     * and drawn.
     */
    private boolean initialiseComponents() {
        try {
            // Form fields
            txtHost = Utilities.createTextField(this, Utilities.getLocalHost().getHostName(), new Rectangle(100, 10, 150, 25), font);
            spnPortNo = Utilities.createSpinner(256, 256, 65000, 1, new Rectangle(100, 35, 70, 30), font);
            //

            // Form labels
            lblPort = Utilities.createLabel("Port No.:", new Rectangle(10, 40, 90, 25), font);
            lblHost = Utilities.createLabel("Host Name:", new Rectangle(10, 10, 90, 25), font);
            //

            //Buttons
            btnHost = Utilities.createButton(this, "Host", KeyEvent.VK_H, new Rectangle(10, 75, btnWidth, btnHeight));
            btnJoin = Utilities.createButton(this, "Join", KeyEvent.VK_J, new Rectangle(10 + btnWidth, 75, btnWidth, btnHeight));
            btnExit = Utilities.createButton(this, "Exit", KeyEvent.VK_E, new Rectangle(btnWidth * 2 + 10, 75, btnWidth, btnHeight));
            //

            return true;
        } catch (Exception e) {
            Log.appendExceptionLog(e, "Initialisation error on main menu screen.");
            return false;
        }
    }

    // </editor-fold>
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHost) {
            try {

                // If the field has been filled in 
                //    get the values and store in RAM
                //    Create JFrame
                // else
                //    Set the text of txtHost to the host name
                //    Throw new null pointer exception to be logged.
                if (!txtHost.getText().isEmpty() && !spnPortNo.getValue().toString().isEmpty()) {
                    String host = txtHost.getText();
                    int port = (int) spnPortNo.getValue();

                    // Creates a jframe with an instance of Instructor, passing in the host and port information.
                    JFrame frmHost = Utilities.CreateJFrame(new Instructor(port), Main.APPLICATION_NAME + " - Instructor (host name: " + host + ", port: " + port + ")",
                            JFrame.HIDE_ON_CLOSE, true, false, 0, 0, 630, 630);
                    frmHost.setLocationRelativeTo(null);
                } else {
                    txtHost.setText(Utilities.getLocalHost().getHostName());
                    throw new NullPointerException("Host name field is empty.");
                }
            } catch (NullPointerException ex) {
                String message = "Error attempting to get one of the values: host name; port number.";
                Log.appendExceptionLog(ex, message);
                JOptionPane.showMessageDialog(this, message, Main.APPLICATION_NAME + " - Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        //
        if (e.getSource() == btnJoin) {
            try {

                // If the field has been filled in 
                //    get the values and store in RAM
                //    Create JFrame
                // else
                //    Set the text of txtHost to the host name
                //    Throw new null pointer exception to be logged.
                if (!txtHost.getText().isEmpty() && !spnPortNo.getValue().toString().isEmpty()) {
                    String host = txtHost.getText();
                    int port = (int) spnPortNo.getValue();

                    // Creates a jframe with an instance of student, passing in the host and port information.
                    JFrame frm = Utilities.CreateJFrame(new Student(host, port), Main.APPLICATION_NAME + " - Student (host name: " + host + ", port: " + port + ")", 
                            JFrame.HIDE_ON_CLOSE, true, false, 0, 0, 201, 181);
                    frm.setLocationRelativeTo(null);
                    
                } else {
                    txtHost.setText(Utilities.getLocalHost().getHostName());
                    throw new NullPointerException("Host name field is empty.");
                }
            } catch (NullPointerException ex) {
                String message = "Error attempting to get one of the values: host name; port number.";
                Log.appendExceptionLog(ex, message);
                JOptionPane.showMessageDialog(this, message, Main.APPLICATION_NAME + " - Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Terminate the currently running java virtual machine.
        if (e.getSource() == btnExit) {
            System.exit(0);
        }
    }
}
