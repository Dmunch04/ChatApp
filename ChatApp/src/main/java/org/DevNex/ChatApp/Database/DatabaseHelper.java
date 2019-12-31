package org.DevNex.ChatApp.Database;

import org.DevNex.ChatApp.Objects.Message;
import org.DevNex.ChatApp.Objects.Room;
import org.DevNex.ChatApp.Objects.User;

import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseHelper
{

    private DatabaseHandler Database;

    private static final String RoomsTable = "Rooms";
    private static final String UsersTable = "Users";
    private static final String MessagesTable = "Messages";

    public DatabaseHelper (DatabaseHandler Database)
    {
        this.Database = Database;
    }

    public boolean RoomExists (UUID RoomID)
    {
        try
        {
            PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + RoomsTable + " WHERE ID=?");
            Statement.setString (1, RoomID.toString ());

            ResultSet Results = Statement.executeQuery ();
            return Results.next ();
        }

        catch (SQLException Error)
        {
            Error.printStackTrace ();
        }

        return false;
    }

    public boolean UserExists (UUID UserID)
    {
        try
        {
            PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE ID=?");
            Statement.setString (1, UserID.toString ());

            ResultSet Results = Statement.executeQuery ();
            return Results.next ();
        }

        catch (SQLException Error)
        {
            Error.printStackTrace ();
        }

        return false;
    }

    public boolean UserExists (String Username)
    {
        try
        {
            PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE Username=?");
            Statement.setString (1, Username);

            ResultSet Results = Statement.executeQuery ();
            return Results.next ();
        }

        catch (SQLException Error)
        {
            Error.printStackTrace ();
        }

        return false;
    }

    public Room GetRoom (UUID ID)
    {
        if (RoomExists (ID))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + RoomsTable + " WHERE ID=?");
                Statement.setString (1, ID.toString ());

                ResultSet Results = Statement.executeQuery ();

                String[] ClientsArray = Results.getString ("Clients").split (",");
                List<UUID> Clients = new ArrayList<UUID> ();
                for (String Client : ClientsArray)
                {
                    Clients.add (UUID.fromString (Client));
                }

                return new Room (ID, Results.getString ("Display"), UUID.fromString (Results.getString ("Creator")), Clients, GetRoomMessages (ID));
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public User GetUser (UUID ID)
    {
        if (UserExists (ID))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE ID=?");
                Statement.setString (1, ID.toString ());

                ResultSet Results = Statement.executeQuery ();

                String[] RoomsArray = Results.getString ("Rooms").split (",");
                List<UUID> Rooms = new ArrayList<UUID> ();
                for (String Room : RoomsArray)
                {
                    Rooms.add (UUID.fromString (Room));
                }

                return new User (Results.getString ("Token"), ID, Results.getString ("Username"), Rooms);
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public User GetUser (String Username)
    {
        if (UserExists (Username))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE Username=?");
                Statement.setString (1, Username);

                ResultSet Results = Statement.executeQuery ();

                String[] RoomsArray = Results.getString ("Rooms").split (",");
                List<UUID> Rooms = new ArrayList<UUID> ();
                for (String Room : RoomsArray)
                {
                    Rooms.add (UUID.fromString (Room));
                }

                return new User (Results.getString ("Token"), UUID.fromString (Results.getString ("ID")), Username, Rooms);
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public String GetPassword (String Username)
    {
        if (UserExists (Username))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE Username=?");
                Statement.setString (1, Username);

                ResultSet Results = Statement.executeQuery ();

                return Results.getString ("Password");
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public Map<UUID, Message> GetRoomMessages (UUID RoomID)
    {
        if (RoomExists (RoomID))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + MessagesTable + " WHERE Room=?");
                Statement.setString (1, RoomID.toString ());

                ResultSet Results = Statement.executeQuery ();

                Map<UUID, Message> Messages = new HashMap<UUID, Message> ();
                Messages.put (
                        UUID.fromString (Results.getString ("ID")),
                        new Message (
                                UUID.fromString (Results.getString ("ID")),
                                RoomID,
                                UUID.fromString (Results.getString ("Sender")),
                                Results.getString ("Content")
                        )
                );

                while (Results.next ())
                {
                    UUID ID = UUID.fromString (Results.getString ("ID"));
                    UUID Sender = UUID.fromString (Results.getString ("Sender"));
                    String Content = Results.getString ("Content");
                    Messages.put (ID, new Message (ID, RoomID, Sender, Content));
                }

                return Messages;
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    // TODO: Make functions
    public void AddRoom (Room Target) { }

    public void RemoveRoom (UUID RoomID) { }

    public void AddUser (User Target) { }

    public void RemoveUser (UUID UserID) { }

    public void AddMessage (Message Target) { }

    public void RemoveMessage (UUID RoomID, UUID MessageID) { }

}
