package org.TeamName.ChatApp;

import org.TeamName.ChatApp.Server.Server;

public class App
{

    private static final String Host = "http://localhost";
    private static final String Port = "6969";

    private static Server Instance;

    public static void main (String[] Args)
    {
        Instance = new Server (Host, Port);
    }

}
