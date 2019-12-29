package org.DevNex.ChatApp.Utils;

import java.sql.Timestamp;
import java.util.Date;

public class Helper
{

    public static Timestamp GetTimestamp ()
    {
        return new Timestamp (new Date ().getTime ());
    }

}
