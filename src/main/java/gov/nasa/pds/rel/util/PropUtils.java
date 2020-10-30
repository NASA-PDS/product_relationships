package gov.nasa.pds.rel.util;

import java.io.FileReader;
import java.util.Properties;

public class PropUtils
{
    public static Properties loadProps(String path) throws Exception
    {
        if(path == null) return null;
        
        Properties props = new Properties();
        FileReader rd = new FileReader(path);
        
        try
        {
            props.load(rd);
        }
        finally
        {
            CloseUtils.close(rd);
        }
        
        return props;
    }

    
    public static Boolean getBoolean(Properties props, String key) throws Exception
    {
        if(props == null) return null;
        
        String str = props.getProperty(key);
        if(str == null) return null;

        if(!str.equals("true") && str.equals("false")) 
        {
            throw new Exception("Property " + key + " has invalid value " + str);
        }
        
        return str.equals("true");
    }

}
