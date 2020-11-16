package gov.nasa.pds.rel.meta.proc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;
import gov.nasa.pds.rel.meta.RDFIri;
import gov.nasa.pds.rel.meta.RDFLiteral;


public class TargetProcessor
{
    private static final Pattern COMET_NAME_P = Pattern.compile("[1-9][0-9]?p/([a-z][a-z]*)");
    
    
    public TargetProcessor()
    {
    }


    public void process(Metadata meta) throws Exception
    {
        if(meta.type.contains("asteroid"))
        {
            String name = extractAsteroidName(meta.title);
            if(name != null) meta.addField(new RDFLiteral("pds:search_name", name.toLowerCase()));
        }
        else if(meta.type.contains("planet"))
        {
            meta.addField(new RDFLiteral("pds:search_name", meta.title.toLowerCase()));
        }
        else if(meta.type.contains("dwarf_planet"))
        {
            processDwarfPlanet(meta);
        }
        else if(meta.type.contains("satellite"))
        {
            processSatellite(meta);
        }
        else if(meta.type.contains("comet"))
        {
            String name = extractCometName(meta.title);
            if(name != null) meta.addField(new RDFLiteral("pds:search_name", name));
        }
        else if(meta.type.contains("star"))
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
            meta.addField(new RDFLiteral("pds:description", meta.title));
            meta.title = name;
        }

        meta.addField(new RDFLiteral("pds:search_name", meta.title.toLowerCase()));
    }


    private void processDwarfPlanet(Metadata meta) throws Exception
    {
        // Extract name
        int idx = meta.title.indexOf(' ');
        String name = meta.title.substring(idx + 1);
        meta.addField(new RDFLiteral("pds:search_name", name.toLowerCase()));
    }
    
    
    private void processSatellite(Metadata meta) throws Exception
    {
        // Normalize name
        meta.addField(new RDFLiteral("pds:search_name", meta.title.replaceAll("[/ ]", "").toLowerCase()));
        // Extract lid of primary
        RDFField desc = meta.getField("pds:description");
        if(desc != null)
        {
            String value = extractLidOfPrimary(desc.value);
            if(value != null)
            {
                meta.addField(new RDFIri("pds:lid_of_primary", "<" + value + ">"));
            }
            else
            {
                System.out.println("[WARN] Could not extract lid of primary from " + meta.lid + "::" + meta.vid);
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
