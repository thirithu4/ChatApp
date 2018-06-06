import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Private chat room GUI.
 */
public class PrivateChat extends JFrame{
    private JPanel privatePanel;
    private JLabel textLabel;
    private JTextArea displayMessage;
    private JScrollPane displayScroll;
    private JTextArea messageText;
    private JButton sendButton;
    private JButton privateHistory;

    public PrivateChat(String receiver, String username){
        super("Private chat with " + receiver);
        this.setLocation(350, 0);
        this.setSize(600, 800);
        this.setFont(new Font("Arial", Font.PLAIN,80));
        privatePanel = new JPanel();
        privatePanel.setLayout(null);
        set_size();
        //Add components to panel
        privatePanel.add(textLabel);
        privatePanel.add(displayMessage);
        privatePanel.add(messageText);
        privatePanel.add(sendButton);
        privatePanel.add(privateHistory);
        getContentPane().add(privatePanel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        //Clear text when user click on the message area
        messageText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageText.setText("");
            }
        });
        //Send message to the display area
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                send_message(username, messageText.getText(), receiver);
                messageText.setText("");
            }
        });
        privateHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                show_history(username, receiver);
            }
        });
    }

    public void set_size(){
        textLabel = new JLabel("Private chat");
        Date date = new Date();
        displayMessage = new JTextArea(date.toString() + "\n");
        displayMessage.setLineWrap(true);
        displayMessage.setWrapStyleWord(true);
        displayMessage.setEditable(false);
        displayScroll = new JScrollPane(displayMessage, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        messageText = new JTextArea("Type your message here...");
        sendButton = new JButton("Send");
        privateHistory = new JButton("View private chat history");

        textLabel.setBounds(30, 30, 200, 30);
        displayMessage.setBounds(30, 100, 440, 400);
        displayScroll.setBounds(30, 100, 440, 400);
        messageText.setLineWrap(true);
        messageText.setWrapStyleWord(true);
        messageText.setBounds(30, 530, 440, 100);
        sendButton.setBounds(150, 630, 200, 30);
        privateHistory.setBounds(270, 30, 200, 30);
    }
    //send message to display on the message area
    public void send_message(String sender, String message, String receiver){
        Date date = new Date();
        SimpleDateFormat only_time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String history = sender+ "@" + only_time.format(date) + ">> " + message + "\n";
        displayMessage.append(history);
        //add to history file too!
        save_to_history(sender, receiver, history);
    }

    public void save_to_history(String sender, String receiver, String message){
        //Compare the name to generate the file name
        String fileName;
        if(sender.compareToIgnoreCase(receiver) < 0)
            fileName = sender + "_" + receiver + ".txt";
        else
            fileName = receiver + "_" + sender + ".txt";
        //Write message into the file
        Writer writer;
        File check = new File(fileName);
        if(!check.exists()){
            try{
                File newFile = new File(fileName);
                writer = new BufferedWriter(new FileWriter(newFile, true));
                writer.write("Message");
                writer.write(message);
                writer.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else {
            try {
                writer = new BufferedWriter(new FileWriter(check, true));
                writer.write(message);
                writer.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void show_history(String sender, String receiver){
        JFrame history= new JFrame("Private chat history.");
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

        String fileName;
        if(sender.compareToIgnoreCase(receiver) < 0)
            fileName = sender + "_" + receiver + ".txt";
        else
            fileName = receiver + "_" + sender + ".txt";
        try {
            File check = new File(fileName);
            FileReader file = new FileReader(fileName);
            if(check.exists())
            {
                BufferedReader reader = new BufferedReader(file);
                String display;
                while((display = reader.readLine()) != null)
                {
                    showHistory.append(display + "\n");
                }
                history.add(showHistory);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String []args)
    {
        PrivateChat privateChat = new PrivateChat("Bobby", "Jimmy");
    }
}

