//Client program for sending information to server and receiving information from server.

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Client
{
    Socket socket;
    int port;
    String host;
    ObjectInputStream readServer;
    ObjectOutputStream writeServer;
    ClientGUI clientGUI;
    ArrayList<ClientGUI.PrivateGUI> privateChat;

    Client(ClientGUI GUI)
    {
        port = 1996;
        host = "localhost";
        clientGUI = GUI;
        privateChat = new ArrayList<>();
    }

    public void start()
    {
        try
        {
            socket = new Socket(host, port);
            display("Connection with server " + socket.getInetAddress() + " : " + socket.getLocalPort());
            readServer = new ObjectInputStream(socket.getInputStream());
            writeServer = new ObjectOutputStream(socket.getOutputStream());
            //this part will be done by ListenFromServer
            //This help user log into the system
            new ListenFromServer().start();

        }
        catch(IOException e)
        {
            display("Error connect to server");
        }
    }

    //send message to server
    public void send_server(String message)
    {
        try
        {
            writeServer.writeObject(message);
        }
        catch (IOException e)
        {
        }
    }

    //display at client side ONLY
    public void display(String message){
        clientGUI.append_msg(message);
    }

    public void append_list(String username){
        clientGUI.append_list(username);
    }

    //for open up private chat window
    public void send_client(String sender, String receiver)
    {
        //For the sender, not for receiver yet
        privateChat.add(new ClientGUI.PrivateGUI(sender, receiver, this)); //new window!
        //tell server to open up another window for receiver
        send_server("private receiver");
        send_server(sender);
        send_server(receiver);
    }
    //open window for receiver
    private void receiver_window(String sender, String receiver){
        /*opposite receiver and sender because it's for the receiver.
          From the receiver perspective, when receiver send message, receiver is the sender.
         */
        privateChat.add(new ClientGUI.PrivateGUI(receiver, sender, this));
    }

    private void find_window(String sender, String receiver, String message){
        for(int i = 0; i < privateChat.size(); ++i){
            ClientGUI.PrivateGUI temp = privateChat.get(i);
            if(temp.match_users(receiver, sender)){
                temp.append_display(sender + " > " + message);
            }
        }
    }

    private void leaving_message(String sender, String receiver){
        for(int i = 0; i < privateChat.size(); ++i){
            ClientGUI.PrivateGUI temp = privateChat.get(i);
            if(temp.match_users(receiver, sender)){
                temp.dialog();
                privateChat.remove(i);
            }
        }
    }

    private void leaving_sender(String sender, String receiver){
        for(int i = 0; i < privateChat.size(); ++i){
            ClientGUI.PrivateGUI temp = privateChat.get(i);
            if(temp.match_users(sender, receiver)){
                privateChat.remove(i);
            }
        }
    }

    public void close()
    {
        try
        {
            if (socket != null) socket.close();
        }
        catch (IOException e)
        {
        }
        try
        {
            if (readServer != null) readServer.close();
        }
        catch (IOException e)
        {
        }
        try
        {
            if (writeServer != null) writeServer.close();
        }
        catch (IOException e)
        {
        }
    }
    //Wait for the message from server
    class ListenFromServer extends Thread
    {
        public void run()
        {
            while(true)
            {
                try
                {
                    String message = (String) readServer.readObject();
                    if(message.compareTo("list") == 0)
                    {
                        String username = (String) readServer.readObject();
                        append_list(username);
                    }
                    else if(message.compareTo("login") == 0){
                        clientGUI.set_logincheck();
                    }
                    else if(message.compareTo("remove") == 0){
                        String removed = (String) readServer.readObject();
                        clientGUI.remove_list(removed);
                    }
                    //open a window for receiver
                    else if(message.compareTo("receiver window") == 0){
                        String sender = (String) readServer.readObject();
                        String receiver = (String) readServer.readObject();
                        receiver_window(sender, receiver);
                    }
                    //Know it's a private message to the receiver
                    else if(message.compareTo("private") == 0){
                        String sender = (String) readServer.readObject();
                        String receiver = (String) readServer.readObject();
                        String msg = (String) readServer.readObject();
                        find_window(sender, receiver, msg);
                    }
                    else if(message.compareTo("leaving") == 0){
                        String sender = (String) readServer.readObject();
                        String receiver = (String) readServer.readObject();
                        leaving_message(sender, receiver);
                    }
                    else if(message.compareTo("leaving sender") == 0){
                        String sender = (String) readServer.readObject();
                        String receiver = (String) readServer.readObject();
                        leaving_sender(sender, receiver);
                    }
                    else {
                        display(message);
                        display("> ");
                    }
                }
                catch (IOException e)
                {
                    display("Server has close the connection" + e);
                    close();
                    break;
                }
                catch(ClassNotFoundException ce)
                {
                }
            }
        }
    }
}







