import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Public chat room GUI.
 * As well as the main page after a user logs in.
 */
public class ChatRoom extends JFrame
{
    private JPanel chatRoomPanel;
    private JList<String> onlineList;
    private JLabel onlineUser;
    private JButton logout;
    private JLabel publicChat;
    private JButton publicHistory;
    private JTextArea message;
    private JButton sendMessage;
    private JTextArea displayMessage;
    private JScrollPane messageScroll;
    private DefaultListModel<String> temp_list;
    //private String username;

    public ChatRoom(String username)
    {
        super("Public chat room");
        this.setLocation(350, 0);
        this.setSize(900, 1000);
        this.setFont(new Font("Arial", Font.PLAIN,80));
        chatRoomPanel = new JPanel();
        chatRoomPanel.setLayout(null);
        set_size();
        chatRoomPanel.add(onlineUser);
        chatRoomPanel.add(logout);
        chatRoomPanel.add(publicChat);
        chatRoomPanel.add(publicHistory);
        chatRoomPanel.add(message);
        chatRoomPanel.add(sendMessage);
        chatRoomPanel.add(messageScroll);
        getContentPane().add(chatRoomPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        //Initialize active user list
        temp_list = new DefaultListModel<>();
        temp_list.addElement(username);
        add_active_user();

        //Detect for new message
        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // super.mouseClicked(e);
                message.setText("");
            }
        });
        //Send button
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send_message(username, message.getText());
                message.setText("Type your message here...");
            }
        });
        //Double click on the username for private chat
        onlineList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() >= 2) {
                    String selected = list.getSelectedValue().toString();
                    PrivateChat privateRoom = new PrivateChat(selected, username);
                    dispose();
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logOut(username);
                System.exit(0);
            }
        });
        publicHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Show the public chat history
                show_history();
            }
        });
    }

    public static void main(String[] args)
    {
        ChatRoom chatroom = new ChatRoom("Jimmy");
    }

    //Set every component in the window
    public void set_size() {
        onlineUser = new JLabel("Active users");
        logout = new JButton("Log out");
        publicChat = new JLabel("Public messages");
        publicHistory = new JButton("View history of public chat");
        message = new JTextArea("Type your message here...");
        sendMessage = new JButton("Send");
        Date date = new Date();
        displayMessage = new JTextArea(date.toString() + "\n");
        messageScroll = new JScrollPane(displayMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //Set the size and location
        onlineUser.setBounds(70, 40, 100, 30);
        logout.setBounds(200, 40, 100, 30);
        publicChat.setBounds(330, 40, 200, 30);
        publicHistory.setBounds(600, 40, 200, 30);
        message.setBounds(70, 550, 500, 100);
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        sendMessage.setBounds(630, 600, 100, 30);
        displayMessage.setBounds(330, 80, 400, 450);
        messageScroll.setBounds(330, 80, 400, 450);
        displayMessage.setEditable(false);
        displayMessage.setLineWrap(true);
        displayMessage.setWrapStyleWord(true);
    }

    public void send_message(String username, String sent_message) {
        //Need to pass in username!
        Date date = new Date();
        SimpleDateFormat only_time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String history = username + "@" + only_time.format(date) + ">> " + sent_message + "\n";
        displayMessage.append(history);
        save_to_history(history);
        //Save message to chat history!!
    }

    public void add_active_user() {
        onlineList = new JList<>(temp_list);
        chatRoomPanel.add(onlineList);
        onlineList.setBounds(70, 80, 200, 450);
        temp_list.addElement("New user!!");
    }

    public void logOut(String username){
        //remove username from the list
        for(int i = 0; i < temp_list.size(); ++i){
            if(temp_list.get(i).compareTo(username) == 0){
                temp_list.remove(i);
            }
        }
    }

    public void save_to_history(String message) {
        File file = new File("PublicChat.txt");
        try {
            Writer writer = new BufferedWriter(new FileWriter(file, true));//Write data into text file
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void show_history(){
        JFrame history= new JFrame("Public chat history.");
        history.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        history.setVisible(true);
        JTextArea showHistory = new JTextArea();
        JScrollPane scroll = new JScrollPane(showHistory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        history.setSize(300, 500);
        history.setLocation(350, 0);
        showHistory.setBounds(30, 30, 260, 460);
        scroll.setBounds(30, 30, 260, 460);
        showHistory.setEditable(false);
        showHistory.setLineWrap(true);
        showHistory.setWrapStyleWord(true);

        try {
            File check = new File("PublicChat.txt");
            FileReader file = new FileReader("PublicChat.txt");
            if(check.exists()){
                BufferedReader reader = new BufferedReader(file);
                String display;
                while((display = reader.readLine()) != null)
                {
                    //display the message
                    //showHistory = new JTextArea();
                    showHistory.append(display + "\n");
                }
                history.add(showHistory);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}