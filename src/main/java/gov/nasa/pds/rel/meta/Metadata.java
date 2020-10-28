package gov.nasa.pds.rel.meta;

import java.util.Set;
import java.util.TreeSet;

public class Metadata
{
    public String lid;
    public String vid;
    public String type;
    public String name;
    public String title;
    public String prodClass;
    
    public String startDate;
    public String stopDate;
        
    public Set<String> lidRefs = new TreeSet<String>();
}
