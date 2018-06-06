import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ClientGUI extends JFrame implements ActionListener, MouseListener
{
    private JPanel clientPanel;
    private JButton register, login, logout, send, history;
    private JTextArea name, message, type;
    private JPasswordField pass;
    private JScrollPane messageScroll;
    //to display a list of online user to choose from
    private JList<String> userList;
    private DefaultListModel<String> tempList;
    private Client client;
    private String username;

    ClientGUI()
    {
        super("Client chat");
        client = new Client(this);
        register = new JButton("Register");
        login = new JButton("Login");
        logout = new JButton("Logout");
        send = new JButton("Send");
        history = new JButton("History");
        name = new JTextArea("Your username");
        pass = new JPasswordField("Your password");
        pass.setEchoChar((char)0);
        message = new JTextArea();
        type = new JTextArea("Type here...");
        clientPanel = new JPanel();
        tempList = new DefaultListModel<>();
        userList = new JList<>(tempList);
        clientPanel.setLayout(null);
        this.setSize(700, 600);
        this.setLocation(500, 300);
        set_size();

        clientPanel.add(register);
        clientPanel.add(login);
        clientPanel.add(logout);
        clientPanel.add(send);
        clientPanel.add(history);
        clientPanel.add(name);
        clientPanel.add(pass);
        clientPanel.add(messageScroll);
        clientPanel.add(userList);
        clientPanel.add(type);

        getContentPane().add(clientPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        login.addActionListener(this);
        logout.addActionListener(this);
        register.addActionListener(this);
        send.addActionListener(this);
        history.addActionListener(this);
        type.addMouseListener(this);
        name.addMouseListener(this);
        pass.addMouseListener(this);
        userList.addMouseListener(this);

        client.start();
    }
    public void append_msg(String message){
        this.message.append(message + "\n");
    }
    //For Client class to add message to text area
    public void add_message(String message){
        client.send_server(message);
    }
    public void append_list(String username){
        tempList.addElement(username);
    }

    private void set_size(){
        name.setBounds(50, 15, 150, 30);
        pass.setBounds(50, 50, 150, 30);
        register.setBounds(50, 90, 100, 30);
        login.setBounds(150, 90, 100, 30);
        logout.setBounds(250, 90, 100, 30);
        logout.setEnabled(false);
        message.setBounds(50, 130, 300,330);
        message.setEditable(false);
        messageScroll = new JScrollPane(message, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageScroll.setBounds(50, 130, 300, 330);
        userList.setBounds(400, 130, 150, 400);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        type.setBounds(50, 480, 200, 70);
        type.setEnabled(false);
        type.setWrapStyleWord(true);
        type.setLineWrap(true);
        send.setBounds(280, 500, 100, 30);
        send.setEnabled(false);
        history.setBounds(420, 90, 100 , 30);
        history.setEnabled(false);
    }

    public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();

        //Check if it's register/ login / logout/ send
        if(o == login){
            username = name.getText();
            client.send_server("login");
            client.send_server(username);
            client.send_server(pass.getText());
        }else if(o == logout){
            client.send_server(username + " has logged out.");
            dispose();
        }else if(o == register){
            username = name.getText();
            client.send_server("register");
            client.send_server(name.getText());
            client.send_server(pass.getText());
        }
        //Display public history
        else if(o == history){
            client.send_server("public history");
        }
        //send message
        else{
            add_message(type.getText());
            type.setText("");
        }
    }

    public void set_logincheck()
    {
        logout.setEnabled(true);
        register.setEnabled(false);
        login.setEnabled(false);
        history.setEnabled(true);
        name.setEnabled(false);
        pass.setEnabled(false);
        send.setEnabled(true);
        type.setEnabled(true);
    }

    public void mouseClicked(MouseEvent event){
        if(event.getSource() == type)
            type.setText("");
        else if(event.getSource() == name)
            name.setText("");
        else if(event.getSource() == pass) {
            pass.setText("");
            pass.setEchoChar('*');
        }
        //Connect to private chat
        else if(event.getClickCount() >= 2)
        {
            if(event.getSource() == userList)
            {
                //PrivateGUI new_private = new PrivateGUI("jimmy", "bob");
                JList list = (JList) event.getSource();
                //Get the username that is double clicked
                String selected = list.getSelectedValue().toString();
                //The selected user cannot be the user itself!!
                if(selected.compareTo(username) != 0)
                {
                    //Let client know to open a new window for the sender (the current user)
                    client.send_client(username, selected);
                }
            }
        }
    }

    public void remove_list(String removed)
    {
        for(int i = 0; i < tempList.size(); ++i)
        {
            String temp = tempList.get(i);
            //find the one that need to be removed
            if(temp.compareTo(removed) == 0)
                tempList.remove(i);
        }
    }
    public void mouseExited(MouseEvent event){
    }
    public void mouseReleased(MouseEvent event){
    }
    public void mousePressed(MouseEvent event){
    }
    public void mouseEntered(MouseEvent event){
    }
    public static void main(String args[])
    {
        ClientGUI client = new ClientGUI();
    }

    static class PrivateGUI extends JFrame implements ActionListener, MouseListener, WindowListener {
        private String sender;
        private String receiver;
        private JPanel privatePanel;
        private JTextArea display, enter;
        private JScrollPane scrollDisplay;
        private JButton privateSend,  historyButton;
        private Client client_private;
        public PrivateGUI(String sender, String receiver, Client client){
            super("Private chat room");
            this.sender = sender;
            this.receiver = receiver;
            client_private = client;
            privatePanel = new JPanel();
            privatePanel.setLayout(null);
            display = new JTextArea(new Date().toString() + "\n");
            append_display("Hello " + sender + "\nWelcome to your private chat room with " + receiver);
            enter = new JTextArea("Enter your message here...");
            privateSend = new JButton("Send");
            historyButton = new JButton("History");
            this.setSize(500, 600);
            this.setLocation(500, 300);

            set_size();

            privatePanel.add(enter);
            privatePanel.add(privateSend);
            privatePanel.add(historyButton);
            privatePanel.add(scrollDisplay);

            enter.addMouseListener(this);
            privateSend.addActionListener(this);
            historyButton.addActionListener(this);
            this.addWindowListener(this);

            getContentPane().add(privatePanel);

            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }

        private void set_size(){
            display.setBounds(50, 50, 350, 350);
            display.setEditable(false);
            scrollDisplay = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollDisplay.setBounds(50, 50, 350, 350);
            display.setLineWrap(true);
            display.setWrapStyleWord(true);
            enter.setBounds(50, 430, 250, 100);
            privateSend.setBounds(330, 470, 100, 30);
            historyButton.setBounds(50, 15, 100, 30);
        }

        //Sender's and receiver's thread will use this to search for the window
        public boolean match_users(String user1, String user2){
            if(this.sender.compareTo(user1) == 0 && this.receiver.compareTo(user2) == 0)
                return true;
            else
                return false;
        }

        public void append_display(String message){
            this.display.append(message + "\n");
        }

        public void actionPerformed(ActionEvent e){
            //send to server and let server send the message to
            if(e.getSource() == privateSend){
                client_private.send_server("private");
                client_private.send_server(this.sender);
                client_private.send_server(this.receiver);
                String msg = enter.getText();
                client_private.send_server(msg);
                //For the sender itself
                append_display(sender + " > " + msg);
                enter.setText("");
            }
            else if(e.getSource() == historyButton){
                client_private.send_server("private history");
                client_private.send_server(this.sender);
                client_private.send_server(this.receiver);
            }
        }

        public void mouseClicked(MouseEvent event){
            if(event.getSource() == enter){
                enter.setText("");
            }
        }
        public void windowClosing(WindowEvent e) {
            //send leaving message to the receiver
            client_private.send_server("private leaving");
            client_private.send_server(sender);
            client_private.send_server(receiver);
            JOptionPane.showMessageDialog(this, "Bye bye " + sender);
            dispose();
        }

        public void dialog(){
            dispose();
            JOptionPane.showMessageDialog(this, receiver + " has left the private chat room.");
        }
        public void mouseExited(MouseEvent event){
        }
        public void mouseReleased(MouseEvent event){
        }
        public void mousePressed(MouseEvent event){
        }
        public void mouseEntered(MouseEvent event){
        }

        public void windowOpened(WindowEvent e) {

        }
        public void windowClosed(WindowEvent e) {

        }
        public void windowIconified(WindowEvent e) {

        }
        public void windowDeiconified(WindowEvent e) {

        }
        public void windowActivated(WindowEvent e) {

        }
        public void windowDeactivated(WindowEvent e) {

        }
    }
}