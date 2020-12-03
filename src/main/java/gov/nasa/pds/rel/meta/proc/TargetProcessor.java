package gov.nasa.pds.rel.meta.proc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;


public class TargetProcessor
{
    private static final Pattern COMET_NAME_P = Pattern.compile("[1-9][0-9]?p/([a-z][a-z\\-]*)");
    private Logger log;
    
    public TargetProcessor()
    {
        log = LogManager.getLogger(this.getClass());
    }


    public void process(Metadata meta) throws Exception
    {
        RDFField field = meta.getField("pds:type");
        if(field == null) 
        {
            log.warn("Missing type for " + meta.lid + "::" + meta.vid);
            return;
        }
        
        if(field.containsValue("asteroid") 
                || field.containsValue("centaur")
                || field.containsValue("trans-neptunian_object"))
        {
            String name = extractAsteroidName(meta.title);
            addName(meta, name);
        }
        else if(field.containsValue("planet"))
        {
            addName(meta, meta.title);
        }
        else if(field.containsValue("dwarf_planet"))
        {
            processDwarfPlanet(meta);
        }
        else if(field.containsValue("satellite"))
        {
            processSatellite(meta);
        }
        else if(field.containsValue("comet"))
        {
            String name = extractCometName(meta.title);
            addName(meta, name);
        }
        else if(field.containsValue("star"))
        {
            processStar(meta);
        }
    }

    
    private void processStar(Metadata meta)
    {
        int idx = meta.title.indexOf(". DEPRECATED. ");
        if(idx > 0)
        {
            String name = meta.title.substring(0, idx);
            meta.addLiteralField("pds:description", meta.title);
            meta.title = name;
        }
    }


    private void processDwarfPlanet(Metadata meta) throws Exception
    {
        // Extract name
        int idx = meta.title.indexOf(' ');
        String name = meta.title.substring(idx + 1);
        addName(meta, name);
    }
    
    
    private void processSatellite(Metadata meta) throws Exception
    {
        // Normalize name
        String normTitle = meta.title.replaceAll("[/ ]", "").toLowerCase();
        if(isValidSatelliteName(normTitle))
        {
            addName(meta, normTitle);
        }

        // Extract lid of primary
        RDFField desc = meta.getField("pds:description");
        if(desc != null)
        {
            String value = extractLidOfPrimary(desc.value);
            if(value != null)
            {
                meta.addIRIField("pds:lid_of_primary", "<" + value + ">");
            }
            else
            {
                System.out.println("[WARN] Could not extract lid of primary from " + meta.lid + "::" + meta.vid);
            }
        }
    }
    
    
    private void addName(Metadata meta, String name)
    {
        if(name == null) return;
        name = name.toLowerCase();
        
        meta.addLiteralField("pds:search_name", name);
        
        String[] tokens = name.split("-");
        if(tokens.length > 1)
        {
            for(String token: tokens)
            {
                meta.addLiteralField("pds:search_name", token);
            }
        }
    }
    
    
    public static String extractCometName(String str)
    {
        if(str == null) return null;
        str = str.toLowerCase();
        
        Matcher match = COMET_NAME_P.matcher(str);
        if(match.find())
        {
            return match.group(1);
        }
        else
        {
            str = str.replaceAll("[\\(\\)]", " ");
            String[] tokens = str.split(" ");
            
            for(String token: tokens)
            {
                if(isValidCometName(token)) return token;
            }
        }
        
        return null;
    }
    

    public static boolean isValidSatelliteName(String name)
    {
        if(name == null) return false;
        
        for(int i = 0; i < name.length(); i++)
        {
            char ch = name.charAt(i);
            if(Character.isDigit(ch) || Character.isWhitespace(ch)) return false;
        }
        
        return true;
    }

        
    public static boolean isValidCometName(String name)
    {
        if(name == null) return false;
        if(name.length() < 2) return false;
        
        for(int i = 0; i < name.length(); i++)
        {
            char ch = name.charAt(i);
            if(Character.isDigit(ch)) return false;
        }
        
        return true;
    }
    
    
    public static String extractAsteroidName(String str)
    {
        if(str == null) return null;
        
        String[] tokens = str.split(" ");
        if(tokens.length == 2)
        {
            String name = tokens[1];
            for(int i = 0; i < name.length(); i++)
            {
                char ch = name.charAt(i);
                if(Character.isDigit(ch)) return null;
                if(ch == '(' || ch == ')') return null;
            }
            return name;
        }
        
        return null;
    }
    
    
    public static String extractLidOfPrimary(String str) throws Exception
    {
        String[] tokens = str.split(";");
        
        for(String token: tokens)
        {
            token = token.trim().toLowerCase();
            if(token.startsWith("lid of primary: "))
            {
                return token.substring(16);
            }
        }
        
        return null;
    }
}
