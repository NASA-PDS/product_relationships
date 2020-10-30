package gov.nasa.pds.rel.util;

import java.io.Closeable;

public class CloseUtils
{
    public static void close(Closeable cl)
    {
        if(cl == null) return;
        
        try
        {
            cl.close();
        }
        catch(Exception ex)
        {
        }
    }

}
