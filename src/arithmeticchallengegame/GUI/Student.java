package arithmeticchallengegame.GUI;
import arithmeticchallengegame.Equation;
import arithmeticchallengegame.Threads.StudentThread;
import arithmeticchallengegame.Utilities;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Daniel Field
 */
public class Student extends JPanel implements ActionListener {

    // <editor-fold desc="Variables">
    
    JTextField txtEquation, txtAnswer;
    
    JButton btnSubmit;
    
    JLabel lblStatus;
    
    // NETWORK RELATED ---------------------------
    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private StudentThread student = null;
    String serverHost;
    int serverPort;
    // ----------------------------------------
    
    Equation currentQuestion;
    
    Font font = Font.decode("Arial");
    
    // </editor-fold>
    
    // <editor-fold desc="Init">

    @SuppressWarnings("LeakingThisInConstructor")
    public Student(String host, int port) {
        
        this.serverHost = host;
        this.serverPort = port;
        
        setLayout(null);
        
        initialiseComponents();
        Utilities.addComponents(this, btnSubmit, txtAnswer, txtEquation, lblStatus);
        
        connect(serverHost, serverPort);
    }
    
    private void initialiseComponents() {
        btnSubmit = Utilities.createButton(this, "Submit", KeyEvent.VK_S, new Rectangle(1, 90, 75, 30));
        
        txtEquation = Utilities.createTextField(this, "", new Rectangle(76, 30, 119, 30), font);
        txtEquation.setEditable(false);
        
        txtAnswer = Utilities.createTextField(this, "", new Rectangle(76, 61, 119, 30), font);
        
        lblStatus = Utilities.createLabel("", new Rectangle(1,121,300,30), font);
    }
    
    // </editor-fold>
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Title
        g.setColor(Color.blue);
        g.fillRect(1, 1, 193, 28);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Student", 65, 20);
        
        // labels
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Question: ", 10, 51);
        g.drawString("Answer: ", 10, 82);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSubmit) {
            send();
            if(Utilities.stripZero(txtAnswer.getText()).equals(Utilities.stripZero(currentQuestion.getCorrectAnswer()+""))) {
                JOptionPane.showMessageDialog(null, "Correct!");
                btnSubmit.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect; Please try again.");
                btnSubmit.setEnabled(true);
            }
            txtAnswer.setText("");
        }
    }
    
    public void connect(String serverName, int serverPort)
    {
        println("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            println("Connected to " + socket.getInetAddress().getHostName());
            open();
        }
        catch (UnknownHostException uhe)
        {
            println("Host unknown: " + uhe.getMessage());
        }
        catch (IOException ioe)
        {
            println("Unexpected exception: " + ioe.getMessage());
        }
    }

    private void send()
    {
        try
        {
            streamOut.writeUTF(Utilities.stripZero(txtAnswer.getText()));
            streamOut.flush();
        }
        catch (IOException ex)
        {
            println("Sending error: " + ex.getMessage());
            close();
        }
    }

    public void handle(String msg)
    {
        if (msg.equals(".bye"))
        {
            println("Good bye. Press EXIT button to exit ...");
            close();
        }
        else if(msg.split(" ").length == 6)
        {
            //println(msg);

//            currentAssocWord++;
//            wordList[currentAssocWord] = new AssocData(msg);
//            for (int i = 0; i < currentAssocWord; i++)
//            {
//                System.out.println("Handle Method: " + i + " - " + wordList[i].words);
//            }
            
            String[] temp = msg.split(" ");
            
            String timeStamp = temp[0] + " " + temp[1];
            float num1 = Float.parseFloat(temp[2]);
            float num2 = Float.parseFloat(temp[4]);
            char op = temp[3].charAt(0);
            float ans = Float.parseFloat(temp[5]);
            
            currentQuestion = new Equation(timeStamp, num1, op, num2, ans);
            txtEquation.setText(Utilities.stripZero(num1+"")+" "+op+" "+Utilities.stripZero(num2+"")+" = ?");
            btnSubmit.setEnabled(true);
        }
    }

    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            student = new StudentThread(this, socket);
        }
        catch (IOException ioe)
        {
            println("Error opening output stream: " + ioe);
        }
    }

    public void close()
    {
        try
        {
            if (streamOut != null)
            {
                streamOut.close();
            }
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException ex)
        {
            println("Error closing ...");
        }
        student.close();
        student.stop();
    }
    
    void println(String msg)
    {
        //display.appendText(msg + "\n");
        lblStatus.setText(msg);
    }
    
    public void stop()
    {
        if (student != null)
        {
            student.stop();
            student = null;
        }
    }
}
