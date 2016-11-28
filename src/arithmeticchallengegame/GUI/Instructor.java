package arithmeticchallengegame.GUI;

import arithmeticchallengegame.Lists.BinaryTree;
import arithmeticchallengegame.Lists.DLinkedEquationList;
import arithmeticchallengegame.Equation;
import arithmeticchallengegame.Log;
import arithmeticchallengegame.Sort;
import arithmeticchallengegame.Lists.TableModel;
import arithmeticchallengegame.Threads.ServerThread;
import arithmeticchallengegame.Threads.InstructorThread;
import arithmeticchallengegame.Utilities;
import java.awt.event.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Callable;
import javax.swing.*;

/**
 *
 * @author Daniel Field
 */
public class Instructor extends JPanel implements ActionListener, Runnable {

    // <editor-fold desc="Variables">
    JLabel lblQuestionCreation, lblStatus;
    JTextField txtFirst, txtSecond, txtAnswer;
    JComboBox cboOperator;
    JButton btnSend;

    JTable tblAskedQuestions;
    TableModel tableModel;
    ArrayList<Equation> askedQuestions = new ArrayList<>();
    JButton btnBubbleSort, btnInsertionSort, btnSelectionSort;

    JScrollPane spLinkedList, tblScrollPane;
    JTextArea txtLinkedList;
    DLinkedEquationList list = new DLinkedEquationList();

    JTextArea txtBinaryTree;
    JScrollPane spBinaryTree;
    JButton btnSave;
    JRadioButton radPreOrder, radInOrder, radPostOrder;
    
    BinaryTree tree = new BinaryTree();

    HashSet<Equation> hs = new HashSet<>();
    
    String[] operators = {"+", "-", "*", "/"};

    Font font = Font.decode("Arial");

    

    boolean hasClickedSelectionSort = false,
            hasClickedInsertionSort = false,
            hasClickedBubbleSort = false;

    // networking variables
    private ServerThread clients[] = new ServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;
    int portNumber;
    
    private Socket socket = null;
    private DataOutputStream streamOut = null;
    private InstructorThread instructorThread = null;
    //
    
    // </editor-fold>
    // <editor-fold desc="Init">
    @SuppressWarnings("LeakingThisInConstructor")
    public Instructor(int port) {

        this.portNumber = port;

        setLayout(null);

        // Import historical equations
        askedQuestions = Log.importArithmeticLog();
        
        // Add equations to the binary tree
        tree.addLeaves(askedQuestions);
        
        // add equations to hash set
        for (Equation askedQuestion : askedQuestions) {
            hs.add(askedQuestion);
        }
        // display equations in console
        displayHashSet();

        // Initialise and add components to the panel.
        initialiseComponents();
        Utilities.addComponents(this, 
                lblQuestionCreation, txtFirst, cboOperator, txtSecond, txtAnswer, btnSend,
                tblScrollPane, btnBubbleSort, btnInsertionSort, btnSelectionSort, spLinkedList, 
                lblStatus, spBinaryTree, btnSave, radPreOrder, radInOrder,
                radPostOrder);
        
        try
        {
            System.out.println("Binding to port " + port + ", please wait  ...");
            
            // create serverSocket
            server = new ServerSocket(port);

            System.out.println("Server started: " + server);
            start();
        }
        catch (IOException ioe)
        {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
        
        connect(Utilities.getLocalHost().getHostName(), port);
    }

    /**
     * This method is to define and draw the components on the frame. This has
     * been done to clean up the constructor, which originally had a lot of code
     * all crammed into it.
     *
     * Return true if the method runs successfully. This is done in case I need
     * to know whether the components were successfully initialised and drawn.
     */
    private boolean initialiseComponents() {
        try { // If successful, return true.

            // Equation form
            lblQuestionCreation = Utilities.createLabel(
                    "<html>Enter question, then click Send.<br><br>"
                    + "First Number:<br><br>"
                    + "Operator:<br><br>"
                    + "Second Number:<br><br>"
                    + "Answer:</html>", new Rectangle(15, 30, 200, 150), font);
            txtFirst = Utilities.createTextField(this, "", new Rectangle(110, 65, 85, 25), font);
            cboOperator = Utilities.createComboBox(this, operators, new Rectangle(110, 90, 85, 25));
            txtSecond = Utilities.createTextField(this, "", new Rectangle(110, 120, 85, 25), font);
            txtAnswer = Utilities.createTextField(this, "", new Rectangle(110, 150, 85, 25), font);
            btnSend = Utilities.createButton(this, "Send", KeyEvent.VK_D, new Rectangle(1, 185, 64, 30));
            //

            // Table
            tableModel = new TableModel(askedQuestions, new String[]{"First Number", "Operator", "Second Number", "Answer"});
            tblScrollPane = new JScrollPane();
            tblScrollPane.setVisible(true);
            tblAskedQuestions = Utilities.createTable(tblScrollPane, this, tableModel, new Rectangle(203, 30, 420, 154));
            tblScrollPane.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
            //

            // Sort buttons
            btnBubbleSort = Utilities.createButton(this, "Bubble Sort", KeyEvent.VK_B, new Rectangle(204, 185, 137, 30));
            btnInsertionSort = Utilities.createButton(this, "Insertion Sort", KeyEvent.VK_I, new Rectangle(343, 185, 137, 30));
            btnSelectionSort = Utilities.createButton(this, "Selection Sort", KeyEvent.VK_S, new Rectangle(483, 185, 137, 30));
            //

            // linked list
            txtLinkedList = Utilities.createTextArea("", new Rectangle(1, 247, 621, 100), font, true);
            spLinkedList = Utilities.createScrollPane(new Rectangle(1, 247, 621, 100));
            spLinkedList.setViewportView(txtLinkedList);
            //

            // Binary tree
            txtBinaryTree = Utilities.createTextArea("", new Rectangle(1, 377, 621, 100), font, true);
            spBinaryTree = Utilities.createScrollPane(new Rectangle(1, 377, 621, 100));
            spBinaryTree.setViewportView(txtBinaryTree);
            btnSave = Utilities.createButton(this, "Save", KeyEvent.VK_A, new Rectangle(1, 480, 64, 30));
            
            radPreOrder = Utilities.createRadioButton("Pre-order", new Rectangle(330, 347, 100, 30), false, font, radPreOrder_ItemStateChanged()); 
            radInOrder = Utilities.createRadioButton("In-order", new Rectangle(430, 347, 100, 30), false, font, radInOrder_ItemStateChanged());    
            radPostOrder = Utilities.createRadioButton("Post-order", new Rectangle(530, 347, 100, 30), false, font, radPostOrder_ItemStateChanged());
 
            ButtonGroup bg = new ButtonGroup();
            bg.add(radPreOrder);
            bg.add(radInOrder);
            bg.add(radPostOrder);
            //

            // status bar
            lblStatus = Utilities.createLabel("Loaded successfully.", new Rectangle(1, 630 - 55, 630 - 2, 30), font);
            //
            
            return true;
        } catch (Exception e) {
            Log.appendExceptionLog(e, "Initialisation error on host screen.");
            return false;
        }
    }

    // Triggered when the radPreOrder item state changes
    //  traverse the tree and display output
    private Callable<Integer> radPreOrder_ItemStateChanged(){
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                
                tree.preOrderTraverseTree(tree.getRoot());
                txtBinaryTree.setText(tree.getTreeAsString());
                
                return 0;
            }
        };
    }
    
    // Triggered when the radInOrder item state changes
    //  traverse the tree and display output
    private Callable<Integer> radInOrder_ItemStateChanged(){
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                
                tree.inOrderTraverseTree(tree.getRoot());
                txtBinaryTree.setText(tree.getTreeAsString());
                
                return 0;
            }
        };
    }
    
    // Triggered when the radPostOrder item state changes
    //  traverse the tree and display output
    private Callable<Integer> radPostOrder_ItemStateChanged(){
        return new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                
                tree.postOrderTraverseTree(tree.getRoot());
                txtBinaryTree.setText(tree.getTreeAsString());

                return 0;
            }
        };
    }
    
    // </editor-fold>
    
    private void displayHashSet() {
        System.out.println("Displaying contents of hash set (Equation answer is the key, equation is the value):");
        Iterator<Equation> tempEqs = hs.iterator();
        for(int i = 0; i < hs.size(); i++){
            Equation tempEq = tempEqs.next();
            System.out.println(tempEq.getAnswer() + " (" + tempEq.getOperandOne() + tempEq.getOperator() + tempEq.getOperandTwo() + ")");
        }
        
        System.out.println("Successfully displayed all " + hs.size() + " items.\r\n");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // This variable will be used to set the status after the actions have been completed.
        String status = "";

        try {
            // If action source is from the combobox, or the text fields
            //     set num1 to txtFirst's value
            //     set num2 to txtSecond's value
            //     Calculate num1 and num2, using specified operator
            //     set answer to the answer calculated
            //     set answer text to the answer, with ".0" removed if necessary.
            //     Display status
            // end if
            if (e.getSource() == cboOperator || e.getSource() == txtFirst || e.getSource() == txtSecond) {
                float num1 = Float.parseFloat(txtFirst.getText());
                float num2 = Float.parseFloat(txtSecond.getText());
                float answer = Utilities.calculateAnswer(num1, cboOperator.getSelectedItem().toString(), num2);

                txtAnswer.setText(Utilities.stripZero(answer + ""));

                status = "Calculated successfully. Click 'send' (hotkey: Alt + S) when you are ready.";
            }

            // If action source is from the send button
            //     set num1 to txtFirst's value
            //     set num2 to txtSecond's value
            //     Calculate num1 and num2, using specified operator
            //     set answer to the answer calculated
            //     Store nums, operator, and answer in equation object
            //     add equation object to tableModel
            //     put equation at the top of the linked list
            //     display linked list
            //     Add equation to binary tree
            //     Add equation to hash set
            //     Display hash set
            //     append log with equation
            //     clear form
            //     Call the server handle method and pass in the equation data
            //     Disable send button
            //     Repaint the jpanel
            // end if
            if (e.getSource() == btnSend) {
                if (txtFirst.getText().length() > 0 && txtSecond.getText().length() > 0) {
                    float num1 = Float.parseFloat(txtFirst.getText());
                    float num2 = Float.parseFloat(txtSecond.getText());
                    float answer = Float.parseFloat(
                            Utilities.calculateAnswer(
                                    num1, cboOperator.getSelectedItem().toString(), num2) + ""
                    );

                    // add to table
                    Equation eq = new Equation(num1, cboOperator.getSelectedItem().toString().charAt(0), num2, answer);
                    tableModel.add(eq);

                    // add to binary tree
                    tree.addLeaf(eq);
                    
                    // add to hash set
                    hs.add(eq);
                    displayHashSet();

                    Log.appendArithmeticLog(eq);

                    txtFirst.setText("");
                    txtSecond.setText("");
                    txtAnswer.setText("");
                    
                    serverHandle(0,eq.getDateTime() + " " + eq.getOperandOne() + " " + eq.getOperator() + " " + eq.getOperandTwo() + " " + eq.getAnswer());
                    
                    btnSend.setEnabled(false);

                    repaint();
                }
            }
            
            // <editor-fold desc="Sorting">
            /*
             The following methods contain code for calculating the 
             speed of the sorting algorithms in milliseconds.
             Some of said code came from: 
             http://stackoverflow.com/questions/9707938/calculating-time-difference-in-milliseconds
             */
            // if source is bubble sort
            //     set hasClickedBubbleSort to the opposite value
            //     sort in asc/desc depending on hasClickedBubbleSort's value
            //     update table
            // end if
            if (e.getSource() == btnBubbleSort) {
                hasClickedBubbleSort = !hasClickedBubbleSort;

                long start_time = System.nanoTime(); //

                Sort.BubbleSort(askedQuestions, false, hasClickedBubbleSort);

                long end_time = System.nanoTime(); //

                tableModel.fireTableDataChanged();

                //
                double difference = (end_time - start_time) / 1e6;
                String asc = hasClickedBubbleSort == true ? "ascending" : "descending";
                System.out.println("Bubble sort took " + difference + " milliseconds to sort in " + asc + " with sortByOperator equal to false");
                //
            }

            // if source is insertion sort
            //     set hasClickedInsertionSort to the opposite value
            //     sort in asc/desc depending on hasClickedInsertionSort's value
            //     update table
            // end if
            if (e.getSource() == btnInsertionSort) {

                hasClickedInsertionSort = !hasClickedInsertionSort;

                long start_time = System.nanoTime(); //

                Sort.InsertionSort(askedQuestions, false, hasClickedInsertionSort);

                long end_time = System.nanoTime(); //

                tableModel.fireTableDataChanged();

                //
                double difference = (end_time - start_time) / 1e6;
                String asc = hasClickedInsertionSort == true ? "ascending" : "descending";
                System.out.println("Insertion sort took " + difference + " milliseconds to sort in " + asc + " with sortByOperator equal to false");
                //
            }

            // if source is Selection sort
            //     set hasClickedSelectionSort to the opposite value
            //     sort in asc/desc depending on hasClickedSelectionSort's value
            //     update table
            // end if
            if (e.getSource() == btnSelectionSort) {
                hasClickedSelectionSort = !hasClickedSelectionSort;

                long start_time = System.nanoTime(); //

                Sort.SelectionSort(askedQuestions, false, hasClickedSelectionSort);

                long end_time = System.nanoTime(); //

                tableModel.fireTableDataChanged();

                //
                double difference = (end_time - start_time) / 1e6;
                String asc = hasClickedSelectionSort == true ? "ascending" : "descending";
                System.out.println("Selection sort took " + difference + " milliseconds to sort in " + asc + " with sortByOperator equal to false");
                //
            }
            // </editor-fold>

            // Set the file name to the appropriate name according to the radio button which is checked
            // write the data from txtBinaryTree to file and overwrite any data currently in the file
            if (e.getSource() == btnSave) {
                Utilities.writeToFile(
                        (radPreOrder.isSelected() == true ? "PreOrder.txt":"") + 
                        (radInOrder.isSelected() == true ? "InOrder.txt":"") +
                        (radPostOrder.isSelected() == true ? "PostOrder.txt":""),
                        txtBinaryTree.getText(), false);
            }            
            
        } catch (NumberFormatException ex) {
            // Display error only if the source is not cboOperator. 
            // This prevents it giving an error while you are picking the operator before filling out the 1st and 2nd numbers.
            if (e.getSource() != cboOperator) {
                status = "Error. Please ensure that the required fields are not empty and that the fields only contain valid characters.";
                Log.appendExceptionLog(ex, status);
            }
        } catch (Exception exc) {
            // Default error status for any other exceptions.
            status = "Looks like there was an error";
            Log.appendExceptionLog(exc, status);
        }
        lblStatus.setText(status); // Display
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color bg = new Color(240, 240, 255);
        
        //Title
        g.setColor(Color.blue);
        g.fillRect(1, 1, 622, 28);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Instructor", 265, 20);

        // Number of historical equations
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString("Historical equations: " + askedQuestions.size(), 10, 20);

        // Equation form
        drawStyledBox(g, bg, Color.blue, new Rectangle(1, 30, 200, 153));

        // Linked list border and text
        drawStyledBox(g, bg, Color.blue, new Rectangle(1, 217, 621, 130));
        g.setColor(Color.black);
        g.drawString("linked List (of all incorrectly answered exercises):", 5, 237);
        
        // Binary tree border and text
        drawStyledBox(g, bg, Color.blue, new Rectangle(1, 347, 621, 130));
        g.setColor(Color.black);
        g.drawString("Binary Tree:", 5, 367);
        //
    }

    private void drawStyledBox(Graphics g, Color background, Color borderColor, Rectangle bounds) {
        g.setColor(background);
        g.fillRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
        g.setColor(borderColor);
        g.drawRect((int) bounds.getX(), (int) bounds.getY(), (int) bounds.getWidth(), (int) bounds.getHeight());
    }

    // <editor-fold desc="Networking stuff" defaultstate="collapsed">
    
    public void connect(String serverName, int serverPort)
    {
        lblStatus.setText("Establishing connection. Please wait ...");
        try
        {
            socket = new Socket(serverName, serverPort);
            lblStatus.setText("Connected to " + socket.getInetAddress().getHostName());
            open();
        }
        catch (UnknownHostException uhe)
        {
            lblStatus.setText("Host unknown: " + serverName+ ":" + serverPort);
        }
        catch (IOException ioe)
        {
            lblStatus.setText("Unexpected exception");
        }
    }
    
    public void open()
    {
        try
        {
            streamOut = new DataOutputStream(socket.getOutputStream());
            instructorThread = new InstructorThread(this, socket);
        }
        catch (IOException ioe)
        {
            lblStatus.setText("Error opening output stream: " + ioe);
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
            lblStatus.setText("Error closing ...");
        }
        instructorThread.close();
        instructorThread.stop();
    }
    
    public void instructorHandle(String msg)
    {
        if (msg.equals(".bye"))
        {
            lblStatus.setText("Good bye. Press EXIT button to exit ...");
            close();
        }
        else {
            try {          
                Equation eq = askedQuestions.get(askedQuestions.size()-1);

                if(msg.equals(Utilities.stripZero(eq.getCorrectAnswer()+"")))
                {
                    lblStatus.setText("Student answered correctly! You can now send a new question.");
                    btnSend.setEnabled(true);
                } else if (!msg.contains("/")) { // Prevents it from running through if statement if msg is an entire equation and not just the answer ('/' is in the datetime)
                    lblStatus.setText("Student answered incorrectly. Waiting to recieve correct answer...");
                    
                    list.getHead().append(eq.getOperandOne(), eq.getOperator(), eq.getOperandTwo(), Float.parseFloat(msg));
                    txtLinkedList.setText(list.displayAsString());
                }
            } catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    // <editor-fold desc="Server" defaultstate="collapsed">
    
    @Override
    public void run() {
        while (thread != null)
        {
            try
            {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            }
            catch (IOException ioe)
            {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }
    
    public void start()
    {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop()
    {
        if (thread != null)
        {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID)
    {
        for (int i = 0; i < clientCount; i++)
        {
            if (clients[i].getID() == ID)
            {
                return i;
            }
        }
        return -1;
    }
    
    public synchronized void serverHandle(int ID, String input)
    {
        if (input.equals(".bye"))
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        else
        {
            for (int i = 0; i < clientCount; i++)
            {
                //if(clients[i].getID() != ID)
                clients[i].send(input);
            }
        }
    }
    
    @Override
    public synchronized void remove(int ID)
    {
        int pos = findClient(ID);
        if (pos >= 0)
        {
            ServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1)
            {
                for (int i = pos + 1; i < clientCount; i++)
                {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try
            {
                toTerminate.close();
            }
            catch (IOException ioe)
            {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    private void addThread(Socket socket)
    {
        if (clientCount < clients.length)
        {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ServerThread(this, socket);
            try
            {
                clients[clientCount].open();
                clients[clientCount].start();
                clientCount++;
            }
            catch (IOException ioe)
            {
                System.out.println("Error opening thread: " + ioe);
            }
        }
        else
        {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }
    // </editor-fold>
    
    // </editor-fold>
}
