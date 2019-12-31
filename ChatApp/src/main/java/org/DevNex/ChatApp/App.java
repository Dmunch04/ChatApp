package org.DevNex.ChatApp;

import org.DevNex.ChatApp.Database.DatabaseHandler;
import org.DevNex.ChatApp.Server.Server;

/*
    App:
    Main entry point for the back-end server
*/
public class App
{

    // Database variables
    private static final String DatabaseHost = "";
    private static final String DatabasePort = "";
    private static final String DatabaseName = "";
    private static final String DatabaseUsername = "";
    private static final String DatabasePassword = "";

    // Server variables
    private static final String Host = "localhost";
    private static final String Port = "6969";
    private static final String SocketPort = "7089";

    private static DatabaseHandler Database;
    private static Server Instance;

    public static void main (String[] Args)
    {
        // TODO: Add the mysql jdbc driver (.jar) to the project! Download from: https://dev.mysql.com/downloads/file/?id=490494
        Database = new DatabaseHandler (DatabaseHost, DatabasePort, DatabaseName, DatabaseUsername, DatabasePassword);
        Instance = new Server (Host, Port, SocketPort, Database);
    }

}
