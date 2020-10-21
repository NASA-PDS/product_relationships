package gov.nasa.pds.rel.util;

public class PdsStringUtils
{
    public static String trim(String str)
    {
        if(str == null) return null;
        str = str.trim();
        
        return str.isEmpty() ? null : str;
    }

}
