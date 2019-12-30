package org.DevNex.ChatApp.Utils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class Helper
{

    public static Timestamp GetTimestamp ()
    {
        return new Timestamp (new Date ().getTime ());
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

}
