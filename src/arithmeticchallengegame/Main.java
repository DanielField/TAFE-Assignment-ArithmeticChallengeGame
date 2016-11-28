package arithmeticchallengegame;

import arithmeticchallengegame.GUI.MainMenu;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Daniel Field
 * 
 * This class exists mainly to load the main menu.
 */
public class Main {
    
    public static final String APPLICATION_NAME = "Arithmetic Challenge Game";
    
    public static void main(String[] args) {

        // Sets the look and feel of the application to nimbus.
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            Log.appendExceptionLog(e, "There was an error while trying to set the look and feel of the application.");
        }
        //

        // Creates a jframe with a new instance of MainMenu.
        JFrame frmMain = Utilities.CreateJFrame(
                new MainMenu(), APPLICATION_NAME, JFrame.EXIT_ON_CLOSE, true, false, 0, 0, 265, 140
        );
        frmMain.setLocationRelativeTo(null); // centres the frame.
        //

        // Create files to store equations and exceptions, if they do not already exist
        Log.createArithmeticLog();
        Log.createExceptionLog();
        //
    }
}
