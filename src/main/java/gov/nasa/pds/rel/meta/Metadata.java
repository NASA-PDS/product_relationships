package gov.nasa.pds.rel.meta;

import java.util.Set;
import java.util.TreeSet;

public class Metadata
{
    public String prodClass;
    public String prodSubClass;
    public String lid;
    public String vid;
    public Set<String> type = new TreeSet<>();
    
    public Set<RDFField> fields = new TreeSet<>();
    public Set<String> lidRefs = new TreeSet<>();
    
    
    public Metadata()
    {
    }


    public void addField(RDFField field)
    {
        fields.add(field);
    }
}
