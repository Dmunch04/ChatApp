package org.DevNex.ChatApp;

import org.DevNex.ChatApp.Server.Server;

public class App
{

    private static final String Host = "localhost";
    private static final String Port = "6969";
    private static final String SocketPort = "7089";

    private static Server Instance;

    public static void main (String[] Args)
    {
        Instance = new Server (Host, Port, SocketPort);
    }

}
