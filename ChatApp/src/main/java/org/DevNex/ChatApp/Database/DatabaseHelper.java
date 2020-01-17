package org.DevNex.ChatApp.Database;

import org.DevNex.ChatApp.ErrorSystem.Error;
import org.DevNex.ChatApp.ErrorSystem.ErrorType;
import org.DevNex.ChatApp.Objects.Message;
import org.DevNex.ChatApp.Objects.Room;
import org.DevNex.ChatApp.Objects.User;
import org.DevNex.ChatApp.Objects.UserStatus;
import org.DevNex.ChatApp.Utils.Helper;

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
                Results.next ();

                String ClientsList = Results.getString ("Clients");
                List<UUID> Clients = new ArrayList<UUID> ();
                if (!ClientsList.equals (".") && !ClientsList.isEmpty ())
                {
                    String[] ClientsArray = ClientsList.split (",");
                    for (String Client : ClientsArray)
                    {
                        Clients.add (UUID.fromString (Client));
                    }
                }

                return new Room (ID, Results.getString ("Display"), UUID.fromString (Results.getString ("Creator")), Helper.DefaultIconID, Clients, GetRoomMessages (ID));
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
                Results.next ();

                String RoomsList = Results.getString ("Rooms");
                List<UUID> Rooms = new ArrayList<UUID> ();
                if (!RoomsList.equals (".") && !RoomsList.isEmpty ())
                {
                    String[] RoomsArray = RoomsList.split (",");
                    for (String Room : RoomsArray)
                    {
                        Rooms.add (UUID.fromString (Room));
                    }
                }

                return new User (Results.getString ("Token"), ID, Results.getString ("Username"), Results.getString ("Password"), Helper.DefaultIconID, Rooms, Helper.GetFromString (Results.getString ("Status")));
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public Object GetUser (String Username)
    {
        if (UserExists (Username))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("SELECT * FROM " + UsersTable + " WHERE Username=?");
                Statement.setString (1, Username);

                ResultSet Results = Statement.executeQuery ();
                Results.next ();

                String RoomsList = Results.getString ("Rooms");
                List<UUID> Rooms = new ArrayList<UUID> ();
                if (!RoomsList.equals (".") && !RoomsList.isEmpty ())
                {
                    String[] RoomsArray = RoomsList.split (",");
                    for (String Room : RoomsArray)
                    {
                        Rooms.add (UUID.fromString (Room));
                    }
                }

                return new User (Results.getString ("Token"), UUID.fromString (Results.getString ("ID")), Username, Results.getString ("Password"), Helper.DefaultIconID, Rooms, Helper.GetFromString (Results.getString ("Status")));
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();

                return new Error (ErrorType.Unknown, "Unknown error occurred while getting account: " + Username);
            }
        }

        return new Error (ErrorType.UserNotFound, "User with that username doesn't exist: " + Username);
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
                Results.next ();

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
                if (Results.next ())
                {
                    //Results.next ();

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

                return new HashMap<UUID, Message> ();
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }

        return null;
    }

    public void AddRoom (Room Target)
    {
        if (!RoomExists (Target.GetID ()))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("INSERT INTO " + RoomsTable + " (ID,Display,Creator,Clients) VALUE (?,?,?,?)");
                Statement.setString (1, Target.GetID ().toString ());
                Statement.setString (2, Target.GetDisplay ());
                Statement.setString (3, Target.GetCreator ().toString ());
                Statement.setString (4, Target.GetClientsString ());

                Statement.executeUpdate ();
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public void UpdateRoom (Room Target)
    {
        if (RoomExists (Target.GetID ()))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("UPDATE " + RoomsTable + " SET ID=?, Display=?, Creator=?, Clients=? WHERE ID=?");
                Statement.setString (1, Target.GetID ().toString ());
                Statement.setString (2, Target.GetDisplay ());
                Statement.setString (3, Target.GetCreator ().toString ());
                Statement.setString (4, Target.GetClientsString ());
                Statement.setString (5, Target.GetID ().toString ());

                Statement.executeUpdate ();
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public boolean RemoveRoom (UUID RoomID)
    {
        if (RoomExists (RoomID))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("DELETE FROM " + RoomsTable + " WHERE ID=?");
                Statement.setString (1, RoomID.toString ());

                Statement.executeQuery ();

                return true;
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
                return false;
            }
        }

        return false;
    }

    public Object AddUser (User Target)
    {
        if (!UserExists (Target.GetID ()) && !UserExists (Target.GetUsername ()))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("INSERT INTO " + UsersTable +" (Token,ID,Username,Password,Rooms,Status) VALUE (?,?,?,?,?,?)");
                Statement.setString (1, Target.GetToken ());
                Statement.setString (2, Target.GetID ().toString ());
                Statement.setString (3, Target.GetUsername ());
                Statement.setString (4, Target.GetPassword ());
                Statement.setString (5, Target.GetRoomsString ());
                Statement.setString (6, Target.GetStatus ().GetStatusName ());

                Statement.executeUpdate ();

                return GetUser (Target.GetID ());
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();

                return new Error (ErrorType.Unknown, "Unknown error occurred while creating account: " + Target.GetUsername ());
            }
        }

        return new Error (ErrorType.UserAlreadyExists, "User with that username already exists: " + Target.GetUsername ());
    }

    public void UpdateUser (User Target)
    {
        if (UserExists (Target.GetID ()))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("UPDATE " + UsersTable + " SET Token=?, ID=?, Username=?, Password=?, Rooms=?, Status=? WHERE ID=?");
                Statement.setString (1, Target.GetToken ());
                Statement.setString (2, Target.GetID ().toString ());
                Statement.setString (3, Target.GetUsername ());
                Statement.setString (4, Target.GetPassword ());
                Statement.setString (5, Target.GetRoomsString ());
                Statement.setString (6, Target.GetStatus ().GetStatusName ());
                Statement.setString (7, Target.GetID ().toString ());

                Statement.executeUpdate ();
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public boolean RemoveUser (UUID UserID)
    {
        if (UserExists (UserID))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("DELETE FROM " + UsersTable + " WHERE ID=?");
                Statement.setString (1, UserID.toString ());

                Statement.executeQuery ();

                return true;
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
                return false;
            }
        }

        return false;
    }

    public void AddMessage (Message Target)
    {
        if (!UserExists (Target.GetID ()))
        {
            try
            {
                PreparedStatement Statement = Database.GetConnection ().prepareStatement ("INSERT INTO " + MessagesTable + " (ID,Room,Sender,Content) VALUE (?,?,?,?)");
                Statement.setString (1, Target.GetID ().toString ());
                Statement.setString (2, Target.GetRoomID ().toString ());
                Statement.setString (3, Target.GetSenderID ().toString ());
                Statement.setString (4, Target.GetContent ());

                Statement.executeUpdate ();
            }

            catch (SQLException Error)
            {
                Error.printStackTrace ();
            }
        }
    }

    public void RemoveMessage (UUID RoomID, UUID MessageID) { }

}
