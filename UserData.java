public class UserData
{
    private String username;
    private String password;

    UserData(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public boolean compare(String username, String password)
    {
        if(this.username.compareTo(username) == 0 && this.password.compareTo(password) == 0)
            return true;
        else
            return false;
    }

    public boolean match(String username)
    {
        if(this.username.compareTo(username) == 0)
            return true;
        else
            return false;
    }
    public void display()
    {
        System.out.println(this.username);
        System.out.println(this.password);
    }

}