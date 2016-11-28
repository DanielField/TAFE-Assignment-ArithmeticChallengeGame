package arithmeticchallengegame.Threads;

import arithmeticchallengegame.GUI.Student;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel Field
 */
public class StudentThread extends Thread
{

    private Socket socket = null;
    private Student student = null;
    private DataInputStream streamIn = null;

    public StudentThread(Student _student, Socket _socket)
    {
        student = _student;
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
            //student.stop();
            student.close();
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
                student.handle(streamIn.readUTF());
            }
            catch (IOException ioe)
            {
                System.out.println("Listening error: " + ioe.getMessage());
                //student.stop();
                student.close();
            }
        }
    }
}

