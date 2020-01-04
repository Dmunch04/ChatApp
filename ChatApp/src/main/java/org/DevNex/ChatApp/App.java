package org.DevNex.ChatApp;

import org.DevNex.ChatApp.Database.DatabaseHandler;
import org.DevNex.ChatApp.Server.Server;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/*
    App:
    Main entry point for the back-end server
*/
public class App
{

    private static DatabaseHandler Database;
    private static Server Instance;

    public static void main (String[] Args)
    {
        Yaml YAMLConfig = new Yaml ();
        Map<String, Object> Config = YAMLConfig.load (App.class.getResourceAsStream ("/Config.yml"));
        Map<String, Object> ServerConfig = (Map<String, Object>) Config.get ("Server");
        Map<String, Object> DBConfig = (Map<String, Object>) Config.get ("Database");

        // TODO: Add the Driver in Maven as well, so it can come in the build!
        Database = new DatabaseHandler (
                DBConfig.get ("Host").toString (),
                DBConfig.get ("Port").toString (),
                DBConfig.get ("Name").toString (),
                DBConfig.get ("Username").toString (),
                DBConfig.get ("Password").toString ()
        );
        Instance = new Server (
                ServerConfig.get ("Host").toString (),
                ServerConfig.get ("Port").toString (),
                ServerConfig.get ("SocketPort").toString (),
                Database
        );
    }

}
