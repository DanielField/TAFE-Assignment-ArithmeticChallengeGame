package arithmeticchallengegame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Daniel Field
 */
public class Log {
    
    static final String EXCEPTION_LOG = "exLog.txt";
    static final String ARITHMETIC_LOG = "ArithmeticLog.txt";
    
    // Creates a log file for logging exceptions.
    // Returns true if successful
    public static boolean createExceptionLog() {
        try {
            File f = new File(EXCEPTION_LOG);

            if(!f.exists()){
                f.getParentFile().mkdirs(); 
                f.createNewFile();
            }
            
            return true;
        } catch(Exception e){
            return false;
        }
    }
    
    // Appends the exception log file with the specified exception.
    // Returns true if successful
    public static boolean appendExceptionLog(Exception e, String error) {
        try {
            String logFormat = "[%1$s]" // DateTime
                    + "[%2$s]\r\n" // Exception name
                    + "Message: %3$s\r\n" // Exception message
                    + "Additional Info: %4$s\r\n\r\n"; // Additional Information
            
            FileWriter out = new FileWriter(EXCEPTION_LOG, true);
            
            out.append(String.format( logFormat, Utilities.getCurrentDateTimeAsString("yyyy/MM/dd HH:mm:ss"), e.getClass(), e.getMessage(), error ));
            out.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Log.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    // Creates a log file for logging the arithmetic.
    // Returns true if successful
    // Reference(s): http://stackoverflow.com/questions/6142901/how-to-create-a-file-in-a-directory-in-java
    public static boolean createArithmeticLog() {
        try {
            File f = new File(ARITHMETIC_LOG);

            if(!f.exists()){
                f.getParentFile().mkdirs(); 
                f.createNewFile();
            }
            
            return true;
        } catch(Exception e){
            return false;
        }
    }
    
    // Appends the arithmetic log file with the specified equation.
    // Returns true if successful
    public static boolean appendArithmeticLog(Equation eq) {
        try {
            String logFormat = "[%1$s]\t " // DateTime
                             + "%2$s %3$s %4$s = %5$s\r\n"; // Arithmetic

            FileWriter out = new FileWriter(ARITHMETIC_LOG, true);

            out.append(String.format( logFormat, Utilities.getCurrentDateTimeAsString("yyyy/MM/dd HH:mm:ss"), eq.getOperandOne(), eq.getOperator(), eq.getOperandTwo(), eq.getAnswer() ));
            out.close();
            return true;
        } catch(Exception e){}
        return false;
    }
    
    public static ArrayList<Equation> importArithmeticLog() {
        try {
            ArrayList<Equation> array = new ArrayList<>();
            FileInputStream fstream = new FileInputStream(ARITHMETIC_LOG);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            
            while((line = br.readLine()) != null) {
                
                String[] elements = line.split(" ");
                Equation eq = new Equation(
                        elements[0].substring(1)+" "+elements[1].substring(0, 7), // date/time
                        Float.parseFloat(elements[2]), // operand 1
                        elements[3].charAt(0), // Operator
                        Float.parseFloat(elements[4]), // Operand 2
                        Float.parseFloat(elements[6])); // answer
                array.add(eq);
            }
            return array;
        } catch(Exception ex) {
            appendExceptionLog(ex,"Error importing arithmetic log.");
            
            return new ArrayList<>();
        }
    }
}
