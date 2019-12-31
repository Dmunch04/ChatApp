package org.DevNex.ChatApp.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler
{

    private String Host, Port, Database, Username, Password;
    private Connection ConnectionInstance;

    public DatabaseHandler (String Host, String Port, String Database, String Username, String Password)
    {
        this.Host = Host;
        this.Port = Port;
        this.Database = Database;
        this.Username = Username;
        this.Password = Password;

        Connect ();
    }

    public void Connect ()
    {
        if (!IsConnected ())
        {
            try
            {
                synchronized (this)
                {
                    Class.forName ("com.mysql.jdbc.Driver");
                    ConnectionInstance = DriverManager.getConnection ("jdbc:mysql://" + Host + ":" + Port + "/" + Database, Username, Password);

                    System.out.println ("Successfully connected to MySQL database!");
                }
            }

            catch (SQLException | ClassNotFoundException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public void Disconnect ()
    {
        if (IsConnected ())
        {
            try
            {
                synchronized (this)
                {
                    ConnectionInstance.close ();
                }
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public Statement CreateStatement ()
    {
        try
        {
            return ConnectionInstance.createStatement ();
        }

        catch (SQLException Error)
        {
            Error.printStackTrace ();
        }

        return null;
    }

    public boolean IsConnected ()
    {
        return ConnectionInstance != null;
    }

    public Connection GetConnection ()
    {
        return ConnectionInstance;
    }

}
