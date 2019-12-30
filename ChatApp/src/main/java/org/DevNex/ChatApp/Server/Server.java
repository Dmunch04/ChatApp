package org.DevNex.ChatApp.Server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import express.Express;
import express.middleware.Middleware;
import org.DevNex.ChatApp.Objects.Data.LoginRegisterData;
import org.DevNex.ChatApp.Sessions.SessionTracker;

import java.io.IOException;


/*
    Server:
    Main server class. This holds the Socket.io and Express server, together with a SessionTracker
*/
public class Server
{

    private Express App;
    private SocketIOServer SocketIO;

    private SessionTracker Tracker;

    private String Host;
    private String Port;
    private String SocketPort;

    public Server (String Host, String Port, String SocketPort)
    {
        this.Host = Host;
        this.Port = Port;
        this.SocketPort = SocketPort;

        this.Tracker = new SessionTracker ();

        // Server Config
        Configuration Config = new Configuration ();
        Config.setHostname (Host);
        Config.setPort (Integer.parseInt (SocketPort));

        // Server JSON Config
        Config.setJsonSupport (new JacksonJsonSupport ());
        Config.getJsonSupport ().addEventMapping ("", SocketIOEvents.LOGIN.GetEventName (), LoginRegisterData.class);

        // Server Socket Config
        SocketConfig SocketConfig = Config.getSocketConfig ();
        SocketConfig.setReuseAddress (true);
        Config.setSocketConfig (SocketConfig);

        // Server SSL Config
        // TODO: Fix error when using SSL
        //Config.setKeyStorePassword ("test1234");
        //Config.setKeyStore (this.getClass ().getResourceAsStream ("/Keystore.jks"));

        this.App = new Express ();
        this.SocketIO = new SocketIOServer (Config);

        //try { App.use (Middleware.statics (this.getClass ().getResource ("/Static/dist/").getPath ())); }
        try { App.use (Middleware.statics (this.getClass ().getResource ("/TestClient/").getPath ())); }
        catch (IOException Error) { Error.printStackTrace (); }

        App.bind (new ServerBindings());
        App.listen (Integer.parseInt (Port));

        new SocketIOHandler (this, SocketIO);

        SocketIO.start ();

        System.out.println ("Server running on: " + Host + ":" + Port + "!");

        try { Thread.sleep (Integer.MAX_VALUE); }
        catch (InterruptedException Error) { Error.printStackTrace (); }

        SocketIO.stop ();
    }

    public SessionTracker GetSessionTracker ()
    {
        return Tracker;
    }

}
