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
            RDFField field = meta.fields.get("pds:name");
            if(field != null)
            {
                int idx = field.value.indexOf(' ');
                field.value = field.value.substring(idx + 1);
            }
        }
        else if(meta.type.contains("asteroid"))
        {
            RDFField field = meta.fields.get("pds:name");
            if(field != null)
            {
                String name = extractAsteroidName(field.value);
                if(name != null) field.value = name; 
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
