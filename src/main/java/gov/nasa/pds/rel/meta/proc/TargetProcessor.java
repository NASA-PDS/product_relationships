package gov.nasa.pds.rel.meta.proc;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;

public class TargetProcessor
{
    public void process(Metadata meta)
    {
        if(meta.type.contains("dwarf_planet"))
        {
            // Extract name
            String value = meta.getTempField("name");
            if(value != null)
            {
                int idx = value.indexOf(' ');
                String name = value.substring(idx + 1);
                meta.addField(new RDFField("pds:search_name", name.toLowerCase()));
            }
        }
        else if(meta.type.contains("asteroid"))
        {
            String value = meta.getTempField("name");
            if(value != null)
            {
                String name = extractAsteroidName(value);
                if(name != null) meta.addField(new RDFField("pds:search_name", name.toLowerCase()));
            }
        }
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
}
