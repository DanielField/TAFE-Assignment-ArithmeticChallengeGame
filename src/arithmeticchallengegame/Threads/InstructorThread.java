/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arithmeticchallengegame.Threads;

import arithmeticchallengegame.GUI.Student;
import arithmeticchallengegame.GUI.Instructor;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel Field
 */
public class InstructorThread extends Thread
{

    private Socket socket = null;
    private Instructor instructor = null;
    private DataInputStream streamIn = null;
    
    public InstructorThread(Instructor _instructor, Socket _socket)
    {
        instructor = _instructor;
        socket = _socket;
        open();
        start();
    }

    public void open()
    {
        try
        {
            streamIn = new DataInputStream(socket.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("Error getting input stream: " + ioe);
            instructor.close();
        }
    }

    public void close()
    {
        try
        {
            if (streamIn != null)
            {
                streamIn.close();
            }
        }
        catch (IOException ioe)
        {
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                instructor.instructorHandle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                instructor.close();
            }
        }
    }
}

