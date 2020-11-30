package gov.nasa.pds.rel.meta;

import java.util.Set;
import java.util.TreeSet;


public class RDFField implements Comparable<RDFField>
{
    public static enum FieldType { Literal, IRI };
    
    public FieldType type;
    public String name;
    public String value;
    public Set<String> values;
    public String dataType;

    
    public RDFField(FieldType type, String name, String value)
    {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    
    public void addValue(String val)
    {
        if(values == null)
        {
            values = new TreeSet<>();
            if(this.value != null)
            {
                values.add(this.value);
            }
        }
        
        values.add(val);
    }
    
    
    public boolean containsValue(String val)
    {
        if(val == null) return false;
        
        if(val.equals(this.value)) return true;
        if(values == null) return false;
        return values.contains(val);
    }


    @Override
    public int compareTo(RDFField other)
    {
        return name.compareTo(other.name);
    }

}
