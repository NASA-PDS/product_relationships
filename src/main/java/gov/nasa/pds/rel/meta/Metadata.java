package gov.nasa.pds.rel.meta;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class Metadata
{
    public String lid;
    public String vid;
    public String title;
    public String prodClass;
    public String prodSubClass;
    
    public Set<String> type = new TreeSet<>();
    public Set<String> keywords = new TreeSet<>();

    public Set<String> lidRefs = new TreeSet<>();
    public Set<String> lidvidRefs = new TreeSet<>();

    protected Map<String, String> tempFields = new TreeMap<>();
    protected Map<String, RDFField> fields = new TreeMap<>();

    
    public Metadata()
    {
    }

    
    public void addField(RDFField field)
    {
        fields.put(field.name, field);
    }


    public RDFField getField(String name)
    {
        return fields.get(name);
    }

    
    public Collection<RDFField> getFields()
    {
        return fields.values();
    }
    
    
    public void addTempField(String name, String value)
    {
        tempFields.put(name, value);
    }


    public String getTempField(String name)
    {
        return tempFields.get(name);
    }

}
