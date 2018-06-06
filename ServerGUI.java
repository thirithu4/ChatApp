import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerGUI extends JFrame implements WindowListener
{
    private JPanel serverPanel;
    private JButton StartStop;
    private JLabel log;
    private JTextArea active, chat;
    private JTextField portNumber;
    private Server server;

    ServerGUI(int port)
    {
        super("Server");
        server = null;
        serverPanel.add(new JLabel("Port number: "));
        portNumber = new JTextField(port);
        serverPanel.add(portNumber);
        //Start or stop the server
        StartStop = new JButton("Start");

        StartStop.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //if it's running, set it to stop
                if(server != null)
                {
                    server.stop();
                    server = null;
                    portNumber.setEditable(false);
                    StartStop.setText("Start");
                    return;
                }
                //Start the server
                int port;
                try
                {
                    port = Integer.parseInt(portNumber.getText().trim());
                }
                catch (Exception ePort)
                {
                    active.append("Invalid port number\n");
                    return;
                }
                //create a new server
                server = new Server(port);
                //Start it as a thread
                new ServerRunning().start();
                StartStop.setText("Stop");
                portNumber.setEditable(false);
            }
        });
        serverPanel.add(StartStop);
        add(serverPanel, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridLayout(2, 1));
        chat = new JTextArea(80, 80);
        chat.setEditable(false);
        appendChat("Chat room.");
        center.add(new JScrollPane(chat));
        active = new JTextArea(80, 80);
        active.setEditable(false);
        appendEvent("Event log.");
        center.add(new JScrollPane(active));
        add(center);

        //Need to know when user click the close button on the frame
        addWindowListener(this);
        setSize(400, 600);
        setVisible(true);
    }

    public void appendChat(String message)
    {
        chat.append(message);
        chat.setCaretPosition(chat.getText().length() - 1);
    }

    public void appendEvent(String message)
    {
        active.append(message);
        active.setCaretPosition(active.getText().length() - 1);
    }

    public void windowClosing(WindowEvent e)
    {
        //Server exit
        if(server != null)
        {
            try
            {
                server.stop();
            }
            catch(Exception eClose)
            {
            }
            server = null;
        }
        dispose();
        System.exit(0);
    }
    public void windowClosed(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    class ServerRunning extends Thread
    {
        public void run()
        {
            server.start();//execute until if fails
            //the server failed
            StartStop.setText("Start");
            portNumber.setEditable(false);
            active.append("Server crashed");
            active.setCaretPosition(active.getText().length() - 1);
            server = null;
        }
    }
}

