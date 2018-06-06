
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.io.File;
import java.awt.EventQueue;
import java.awt.Font;

public class Login extends JFrame
{
    private Alist user_data;
    private JPanel loginPanel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JButton registerButton;
    private Socket socket;
    //private ArrayList<UserData> user_list = new ArrayList<UserData>();

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    Login frame = new Login();
                    //frame.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login()
    {
        //Title of the window
        super("CHAT APP!");
        this.setSize(650, 700);
        this.setLocation(500, 300);
        this.setFont(new Font("Arial", Font.PLAIN,80));
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        usernameLabel = new JLabel("USERNAME:");
        passwordLabel = new JLabel("PASSWORD:");
        loginButton = new JButton("LOG IN");
        passwordField = new JPasswordField(15);
        usernameField = new JTextField(15);
        registerButton = new JButton("SIGN UP FOR ACCOUNT");
        usernameLabel.setBounds(30, 120, 100, 30);
        passwordLabel.setBounds(30, 200, 100, 30);
        usernameField.setBounds(180, 120, 180, 30);
        passwordField.setBounds(180, 200, 180, 30);
        loginButton.setBounds(100, 300, 80, 30);
        registerButton.setBounds(50, 30, 250, 30);

        //Add components to panel
        loginPanel.add(usernameLabel);
        loginPanel.add(passwordLabel);
        loginPanel.add(loginButton);
        loginPanel.add(passwordField);
        loginPanel.add(usernameField);
        loginPanel.add(registerButton);
        getContentPane().add(loginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        user_data = new Alist();

        loginButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    //Read in input
                    String userinput = usernameField.getText();
                    String passinput = passwordField.getText();

                try {
                    if (user_data.compare(userinput, passinput))
                    {

                        ChatRoom newRoom = new ChatRoom(userinput);
                        dispose();
                        return;
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "WRONG USERNAME/PASSWORD.");
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        registerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try {
                    Register newUser = new Register();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

}
