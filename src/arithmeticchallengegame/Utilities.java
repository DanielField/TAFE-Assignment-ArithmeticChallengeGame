package arithmeticchallengegame;

import arithmeticchallengegame.Lists.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.net.*;
import java.text.*;
import java.util.Calendar;
import java.util.concurrent.Callable;
import javax.swing.*;

/**
 * This class is to store methods that could be potentially re-used. References
 * specified above each method, where applicable.
 *
 * @author Daniel Field
 */
public class Utilities {

    // <editor-fold desc="Creating components" defaultstate="collapsed">
    // Initialises and instantiates a JLabel, sets the bounds to the rectangle dimensions specified, sets the text, and sets the font.
    // Returns the JLabel.
    static public JLabel createLabel(String text, Rectangle rect, Font font) // No background colour
    {
        JLabel lbl = new JLabel();
        lbl.setBounds(rect);
        lbl.setText(text);
        lbl.setFont(font);
        return lbl;
    }

    // Initialises and instantiates a JLabel, sets the bounds to the rectangle dimensions specified, 
    // sets the text, sets the font, and sets the background and foreground colours.
    // Returns the JLabel.
    static public JLabel createLabel(String text, Rectangle rect, Font font, Color backColour, Color foreColour) {
        JLabel lbl = new JLabel();
        lbl.setBounds(rect);
        lbl.setText(text);
        lbl.setOpaque(true);
        lbl.setBackground(backColour);
        lbl.setForeground(foreColour);
        lbl.setFont(font);
        return lbl;
    }

    // Initialises and instantiates a JButton, adds the specified actionListener, sets the bounds, sets the mnemonic, and sets the text.
    // returns the JButton
    static public JButton createButton(ActionListener actionListener, String text, int MnemonicIndex, Rectangle bounds) {
        JButton btn = new JButton(text);
        btn.setBounds(bounds);
        btn.setMnemonic(MnemonicIndex);
        btn.addActionListener(actionListener);
        btn.setBackground(new Color(230, 230, 230));
        return btn;
    }

    // Creates a JTextField with the specified actionListener, text, rectangle (holds the size and position), and font.
    // Returns JTextField
    static public JTextField createTextField(ActionListener actionListener, String text, Rectangle rect, Font font) {
        JTextField txt = new JTextField();
        txt.setBounds(rect);
        txt.setText(text);
        txt.setFont(font);
        txt.addActionListener(actionListener);
        return txt;
    }

    // Creates a JTextArea with the specified text, rectangle (holds the size and position), font, and boolean to determine whether to wrap the words.
    // Returns JTextArea
    static public JTextArea createTextArea(String text, Rectangle rect, Font font, boolean wordWrap) {
        JTextArea txt = new JTextArea();

        txt.setWrapStyleWord(wordWrap); // Changes the wrap style so it wraps after the word and not the character.
        txt.setLineWrap(wordWrap);

        txt.setBounds(rect);
        txt.setText(text);
        txt.setFont(font);

        return txt;
    }

    // Creates a JScrollPane with the specified size and position.
    // Returns the JScrollPane.
    static public JScrollPane createScrollPane(Rectangle rect) {
        JScrollPane sp = new JScrollPane();
        sp.setBounds(rect);
        return sp;
    }

    // Reference: http://stackoverflow.com/questions/15880844/how-to-limit-jspinner
    // Creates a JSpinner with the specified default value, minimum value, maximum value, rectangle (size and position), and font.
    // Returns a JSpinner object.
    static public JSpinner createSpinner(int def, int min, int max, int inc, Rectangle rect, Font font) {
        SpinnerModel mod = new SpinnerNumberModel(def, min, max, inc);
        JSpinner spn = new JSpinner(mod);
        spn.setBounds(rect);
        spn.setFont(font);
        return spn;
    }

    // References: http://www.tutorialspoint.com/swing/swing_jradiobutton.htm
    //             http://stackoverflow.com/questions/4685563/how-to-pass-a-function-as-a-parameter-in-java
    // Creates a JRadioButton with the specified text, position, location, 
    // default state (checked, unchecked), font, and the event that will be called on itemStateChanged.
    // Returns the JRadioButton object
    public static JRadioButton createRadioButton(String text, Rectangle rect, boolean defaultState, Font font, final Callable<Integer> event_itemStateChanged) {
        JRadioButton rad = new JRadioButton(text, defaultState);
        rad.setFont(font);
        rad.setBounds(rect);

        rad.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    event_itemStateChanged.call(); // Call event method
                } catch (Exception ex) {
                    Log.appendExceptionLog(ex, "Error trying to call radio button event.");
                }
            }
        });

        return rad;
    }

    // Creates a JComboBox with the specified action listener, string array (to store the values), and rectangle (size and position)
    // Returns JComboBox.
    static public JComboBox createComboBox(ActionListener actionListener, String[] strings, Rectangle rect) {
        JComboBox cbo = new JComboBox();
        cbo.setBounds(rect);
        cbo.setModel(new DefaultComboBoxModel(strings));
        cbo.addActionListener(actionListener);
        return cbo;
    }

    // Creates a JTable and puts itself in the specified JScrollPane, using the specified action listener, data (using the TableModel class located in TableModel.java)
    // and uses the specified size and position (Rectangle).
    // Returns JTable.
    static public JTable createTable(JScrollPane sp, ActionListener actionListener, TableModel data, Rectangle rect) {
        JTable tbl = new JTable(data);
        sp.setBounds(rect);
        sp.setViewportView(tbl);
        return tbl;
    }

    // adds the specified components to the specified container.
    public static void addComponents(Container container, Component... components) {
        for (Component c : components) {
            container.add(c);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Arithmetic methods" defaultstate="collapsed">
    // This method calculates equations with two operands, and one operator.
    // Usage: calculateAnswer(<number>, "<operator (+,-,/,*)>", <number>);
    // Example: "calculateAnswer(6.8,"/",2);." This would return 3.4 as a float.
    static public float calculateAnswer(float num1, String operator, float num2) {
        float answer = 0;

        switch (operator) {
            case "+":
                answer = num1 + num2;
                break;
            case "-":
                answer = num1 - num2;
                break;
            case "*":
                answer = num1 * num2;
                break;
            case "/":
                answer = num1 / num2;
                break;
        }
        return answer;
    }

    // Strips the ".0" from the end of a string. This is useful if you have converted a float,
    // decimal, or double into a string and it has ".0" at the end.
    // returns a string.
    // Example: "stripZero("42.0");". This will return "42".
    static public String stripZero(String str) {
        if (str.endsWith(".0")) {
            return str.substring(0, str.length() - 2);
        } else {
            return str;
        }
    }
    // </editor-fold>

    // <editor-fold desc="Networking" defaultstate="collapsed">
    // Easier to remember this method instead of trying to remember how to use InetAddress.getLocalHost()
    public static InetAddress getLocalHost() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
            return address;
        } catch (UnknownHostException ex) {
            Log.appendExceptionLog(ex, "Error attempting to get local host.");
            return null;
        }
    }

    // </editor-fold>
    
    // <editor-fold desc="Misc. methods" defaultstate="collapsed">
    // Returns the date and time in the format specified.
    public static String getCurrentDateTimeAsString(String format) {
        DateFormat dateTimeFormat = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();

        return dateTimeFormat.format(cal.getTime());
    }

    // Creates a JFrame and returns it. This would best be used with overloads to allow 
    // the user to specify different parameters. Though it isn't completely necessary for this assignment.
    public static JFrame CreateJFrame(JPanel panel, String title, int DefaultCloseOperation, boolean isVisible, boolean isResizable, int x, int y, int width, int height) {
        JFrame frmHost = new JFrame();
        frmHost.add(panel);
        frmHost.setTitle(title);
        frmHost.setDefaultCloseOperation(DefaultCloseOperation);
        frmHost.pack();
        frmHost.setVisible(isVisible);
        frmHost.setResizable(isResizable);
        frmHost.setSize(width, height);
        frmHost.setLocation(x, y);
        return frmHost;
    }

    // </editor-fold>
    
    // <editor-fold desc="File system" defaultstate="collapsed">
    
    // Writes data to specified file. Appends if appendData equals true, else it overwrites the data in the file (if it already contains data).
    // Returns true if successful, else it returns false.
    public static boolean writeToFile(String file, String data, boolean appendData) {
        try {
            FileWriter out = new FileWriter(file, appendData);
            out.append(data);
            out.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    // </editor-fold>
}
