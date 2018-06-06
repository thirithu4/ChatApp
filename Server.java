import sun.security.x509.UniqueIdentity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import com.sun.tools.javadoc.Start;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

    public class Server
    {
        // a unique ID for each connection
        private static int uniqueId;
        private ArrayList<ClientThread> Clients;
        // if I am in a GUI
        private ServerGUI serverG;
        private SimpleDateFormat date;
        private int port;
        // the boolean that will be turned off to stop the server
        private boolean keepGoing;

        Server(int port)
        {
            serverG = new ServerGUI(port);
            this.port = port;
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Clients = new ArrayList<ClientThread>();
        }

        public void start()
        {
            keepGoing = true;
            try
            {
                //Socket for server
                ServerSocket serverSocket = new ServerSocket(port);

                //Wait for connections
                while(keepGoing)
                {
                    //Waiting message
                    display("Server Waiting for clients on port" + port + ".");
                    //accept connection
                    Socket socket = serverSocket.accept();
                    //asked to stop
                    if(!keepGoing)
                        break;
                    //A new thread of a new client
                    ClientThread temp_thread = new ClientThread(socket);
                    Clients.add(temp_thread);
                    temp_thread.start();
                }
                try
                {
                    serverSocket.close();
                    for(int i = 0; i < Clients.size(); ++i)
                    {
                        ClientThread thread = Clients.get(i);
                        try
                        {
                            thread.sInput.close();
                            thread.sOutput.close();
                            thread.socket.close();
                        }
                        catch(IOException e)
                        {
                        }
                    }
                }
                catch (Exception e)
                {
                    display("Exception closing the server and clients: " + e);
                }
            }
            catch(IOException e)
            {
                String message = date.format(new Date()) + "Exception on new ServerSocket" + e + "\n";
                display(message);
            }
        }
        //For gui to stop the server
        public void stop()
        {
            keepGoing = false;
            try
            {
                new Socket("localhost", port);
            }
            catch (Exception e)
            {
            }
        }

        public void display(String message)
        {
            String time = date.format(new Date()) + " " + message;
            serverG.appendEvent(time);
        }

        private synchronized void broadcast(String message)
        {
            //add time to message
            String time = date.format(new Date());
            String append_msg = time + " " + message + "\n";
            //Display message
            serverG.appendChat(append_msg);

            for(int i = Clients.size(); --i >= 0;)
            {
                ClientThread temp_thread = Clients.get(i);
                if(!temp_thread.displayMSG(append_msg))
                {
                    Clients.remove(i);
                    display("Disconnected Client " + temp_thread.username + " removed from the list.");
                }
            }
        }

        synchronized void logout(String username)
        {
            for(int i = 0; i < Clients.size(); ++i)
            {
                ClientThread temp_thread = Clients.get(i);
                //Found it
                if(temp_thread.username.compareTo(username) == 0)
                {
                    Clients.remove(i);
                    return;
                }
            }
        }
        class ClientThread extends Thread
        {
            // the socket where to listen/talk
            Socket socket;
            ObjectInputStream sInput;
            ObjectOutputStream sOutput;
            String username;
            String message;
            String date;

            ClientThread(Socket socket)
            {
               /*
               unique id for every client
               id = ++uniqueId;
               */
            }

            private boolean displayMSG(String message){
                return false;
            }
        }

    }

