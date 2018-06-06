import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Register extends JFrame
{
    private JPanel registerPanel;
    private JTextField username;
    private JPasswordField password;
    private JPasswordField reenterPass;
    private JLabel userLabel;
    private JLabel passlabel;
    private JLabel rePass;
    private JButton registerButton;
    private Alist user_data;

    public Register() throws IOException {
        super("REGISTER FOR ACCOUNT.");
        this.setSize(700, 700);
        this.setLocation(500, 500);
        this.setFont(new Font("Arial", Font.PLAIN,80));
        registerPanel = new JPanel();
        registerPanel.setLayout(null);
        username = new JTextField(15);
        password = new JPasswordField(15);
        reenterPass = new JPasswordField(15);
        userLabel = new JLabel("USERNAME:", JLabel.LEFT);
        passlabel = new JLabel("PASSWORD:", JLabel.LEFT);
        rePass = new JLabel("RE-ENTER PASSWORD:", JLabel.LEFT);
        registerButton = new JButton("REGISTER");

        userLabel.setBounds(30, 60, 200, 30);
        passlabel.setBounds(30, 120, 200, 30);
        rePass.setBounds(30, 180, 200, 30);
        username.setBounds(180, 60, 180, 30);
        password.setBounds(180, 120, 180, 30);
        reenterPass.setBounds(180, 180, 180, 30);
        registerButton.setBounds(150, 300, 100, 30);
        //Add components to the panel
        registerPanel.add(username);
        registerPanel.add(password);
        registerPanel.add(reenterPass);
        registerPanel.add(userLabel);
        registerPanel.add(passlabel);
        registerPanel.add(rePass);
        registerPanel.add(registerButton);

        getContentPane().add(registerPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        user_data = new Alist("userData.txt");

        registerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String inputuser = username.getText();
                String inputpass = password.getText();
                String inputpass2 = reenterPass.getText();

                if(!check_password(inputpass, inputpass2))
                    JOptionPane.showMessageDialog(null, "PASSWORDS DON'T MATCH!");

                else {
                    try
                    {
                        if (!user_data.match(inputuser))
                        {
                            JOptionPane.showMessageDialog(null, "USERNAME ALREADY IN USE.");
                        }
                        else{
                            user_data.add_newUser(inputuser, inputpass);
                        }
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }

            private void dispose(){}
        });
    }

    public boolean check_password(String password, String password2)
    {
        if(password2.equals(password))
            return true;
        else
            return false;
    }
}