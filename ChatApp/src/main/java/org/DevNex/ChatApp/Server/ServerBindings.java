package org.DevNex.ChatApp.Server;

import express.DynExpress;
import express.http.RequestMethod;
import express.http.request.Request;
import express.http.response.Response;
import org.DevNex.ChatApp.Database.DatabaseHelper;
import org.DevNex.ChatApp.ErrorSystem.Error;
import org.DevNex.ChatApp.ErrorSystem.ErrorType;
import org.DevNex.ChatApp.Objects.Message;
import org.DevNex.ChatApp.Objects.Room;
import org.DevNex.ChatApp.Objects.User;
import org.DevNex.ChatApp.Utils.Helper;

import java.nio.file.Paths;
import java.util.UUID;

public class ServerBindings
{

    @DynExpress (context = "/")
    public void GetIndex (Request Request, Response Response)
    {
        Response.send (Paths.get (this.getClass ().getResource ("/Static/dist/index.html").getPath ()));
        //Response.send (Paths.get (this.getClass ().getResource ("/TestClient/index.html").getPath ()));
    }


    /* API Handlers
     * These will handle API requests
     */

    @DynExpress (context = "/api/rooms", method = RequestMethod.POST)
    public void CreateRoom (Request Request, Response Response)
    {
        //String Token = Request.getHeader ("Token").get (0); // Might not work
        // TODO: Validate token, and do it in the other functions as well
        // TODO: Find a good way to make helper functions that do these things (create room, send message, get user, etc.) and also use them in socket events too
    }

    @DynExpress (context = "/api/rooms/:id", method = RequestMethod.GET)
    public void GetRoom (Request Request, Response Response)
    {
        DatabaseHelper DBHelper = SocketIOHandler.GetDBHelper ();

        UUID RoomID = null;

        try
        {
            RoomID = UUID.fromString (Request.getParam ("id"));
        }

        catch (Exception Error)
        {
            Error GeneratedError = new Error (ErrorType.Unknown, "Unknown error: " + Error.getMessage ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));

            return;
        }

        if (DBHelper.RoomExists (RoomID))
        {
            Room TargetRoom = DBHelper.GetRoom (RoomID);

            Response.send (Helper.MapToJSON (TargetRoom.ToMap ()));
        }

        else
        {
            Error GeneratedError = new Error (ErrorType.RoomNotFound, "Room with ID was not found: " + RoomID.toString ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));
        }
    }

    @DynExpress (context = "/api/rooms/:id/messages", method = RequestMethod.POST)
    public void PostMessage (Request Request, Response Response)
    {
        UUID RoomID = UUID.fromString (Request.getParam ("id"));
    }

    @DynExpress (context = "/api/rooms/:id/messages/:messageid", method = RequestMethod.GET)
    public void GetMessage (Request Request, Response Response)
    {
        DatabaseHelper DBHelper = SocketIOHandler.GetDBHelper ();

        UUID RoomID = null;
        UUID MessageID = null;

        try
        {
            RoomID = UUID.fromString (Request.getParam ("id"));
            MessageID = UUID.fromString (Request.getParam ("messageid"));
        }

        catch (Exception Error)
        {
            Error GeneratedError = new Error (ErrorType.Unknown, "Unknown error: " + Error.getMessage ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));

            return;
        }

        if (DBHelper.RoomExists (RoomID))
        {
            Room TargetRoom = DBHelper.GetRoom (RoomID);

            if (TargetRoom.GetMessages ().containsKey (MessageID))
            {
                Message TargetMessage = TargetRoom.GetMessage (MessageID);

                Response.send (Helper.MapToJSON (TargetMessage.ToMap ()));
            }

            else
            {
                Error GeneratedError = new Error (ErrorType.MessageNotFound, "Message with ID was not found: " + MessageID.toString ());

                Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));
            }
        }

        else
        {
            Error GeneratedError = new Error (ErrorType.RoomNotFound, "Room with ID was not found: " + RoomID.toString ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));
        }
    }

    @DynExpress (context = "/api/rooms/:id/users/:userid", method = RequestMethod.GET)
    public void GetUser (Request Request, Response Response)
    {
        DatabaseHelper DBHelper = SocketIOHandler.GetDBHelper ();

        UUID RoomID = null;
        UUID UserID = null;

        try
        {
            RoomID = UUID.fromString (Request.getParam ("id"));
            UserID = UUID.fromString (Request.getParam ("userid"));
        }

        catch (Exception Error)
        {
            Error GeneratedError = new Error (ErrorType.Unknown, "Unknown error: " + Error.getMessage ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));

            return;
        }

        if (DBHelper.RoomExists (RoomID))
        {
            Room TargetRoom = DBHelper.GetRoom (RoomID);

            if (TargetRoom.HasClient (UserID) && DBHelper.UserExists (UserID))
            {
                User TargetUser = DBHelper.GetUser (UserID);

                Response.send (Helper.MapToJSON (TargetUser.ToSafeMap ()));
            }

            else
            {
                Error GeneratedError = new Error (ErrorType.UserNotFound, "User with ID was not found: " + UserID.toString ());

                Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));
            }
        }

        else
        {
            Error GeneratedError = new Error (ErrorType.RoomNotFound, "Room with ID was not found: " + RoomID.toString ());

            Response.send (Helper.MapToJSON (GeneratedError.ToMap ()));
        }
    }

}
