package org.DevNex.ChatApp.Utils;

import org.DevNex.ChatApp.Objects.UserStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Helper
{

    private static final String Characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?-#$@";

    public static Timestamp GetTimestamp ()
    {
        return new Timestamp (new Date ().getTime ());
    }

    public static String GetSalt (String Hash)
    {
        String[] hash_parts = Hash.split ("\\$");
        return String.format("$%s$%s$%s", hash_parts[1], hash_parts[2], hash_parts[3].substring (0, 22));
    }

    public static UUID MakeUUID (String UUIDString) {
        String[] Parts = {
                UUIDString.substring (0, 7),
                UUIDString.substring (9, 12),
                UUIDString.substring (14, 17),
                UUIDString.substring (19, 22),
                UUIDString.substring (24, 35)
        };

        long M1 = Long.parseLong (Parts[0], 16);
        long M2 = Long.parseLong (Parts[1], 16);
        long M3 = Long.parseLong (Parts[2], 16);
        long LSB1 = Long.parseLong (Parts[3], 16);
        long LSB2 = Long.parseLong (Parts[4], 16);
        long MSB = (M1 << 32) | (M2 << 16) | M3;
        long LSB = (LSB1 << 48) | LSB2;
        return new UUID (MSB, LSB);
    }

    public static Object CreateClass (Class<?> Target, Map<String, String> Args)
    {
        try
        {
            Constructor<?> TargetConstructor =  Target.getConstructor (Map.class);
            return TargetConstructor.newInstance (Args);
        }

        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException Error)
        {
            Error.printStackTrace ();
        }

        return null;
    }

    public static String GenerateToken (int Size)
    {
        StringBuilder Token = new StringBuilder ();

        while (Size-- != 0) {
            int Character = (int) (Math.random () * Characters.length ());
            Token.append (Characters.charAt (Character));
        }

        return Token.toString();
    }

    public static UserStatus GetFromString (String Status)
    {
        switch (Status)
        {
            case "Offline": return UserStatus.OFFLINE;
            case "Online": return UserStatus.ONLINE;
            case "Away": return UserStatus.AWAY;
            case "Do Not Disturb": return UserStatus.DO_NOT_DISTURB;
            default: return UserStatus.OFFLINE;
        }
    }

}
