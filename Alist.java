import java.io.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;

public class Alist
{
    private ArrayList<UserData> user_list;
    private String filename;

    public Alist(String filename) throws IOException
    {
        user_list = new ArrayList<>();
        this.filename = filename;
        Read_file(filename);
    }

    public Alist()
    {
        user_list = new ArrayList<>();
        this.filename = "userData.txt";
    }

    public void Read_file(String filename)throws IOException
    {
        File file = new File(filename);
        //File file = new File("userData.txt");

        if(file.exists())
        {
            try
            {
                String userFile;
                String passFile;
                Scanner read_file = new Scanner(file);
                while (read_file.hasNext())
                {
                    userFile = read_file.nextLine();
                    passFile = read_file.nextLine();
                    user_list.add(new UserData(userFile, passFile));
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //Find matching username and keyword to be able to login
    public boolean compare (String username, String password) throws IOException
    {
        Read_file("userData.txt");
        for(int i = 0; i < user_list.size(); ++i)
        {
            if(user_list.get(i).compare(username, password))
                return true;
        }
        return false;
    }

    public void setFileName(String filename)
    {
        this.filename = filename;
    }

    //Adding new user or register
    public void add_newUser(String username, String password)
    {
        user_list.add(new UserData(username, password));
        try
        {
            FileWriter writeFile = new FileWriter(filename, true);
            writeFile.write(username + "\r\n" + password + "\r\n");
            writeFile.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //Check for username avalability to be able to register
    public boolean match(String username) throws IOException
    {
       Read_file("userData.txt");
        for(int i = 0; i < user_list.size(); ++i)
        {
            //return false that means the username is not available
            if(user_list.get(i).match(username))
                return false;
        }
        return true;
    }

    public void display()
    {
        for (int i = 0; i< user_list.size(); ++i)
        {
            user_list.get(i).display();
        }
    }
}
