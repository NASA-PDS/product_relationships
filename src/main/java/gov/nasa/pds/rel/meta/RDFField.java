package gov.nasa.pds.rel.meta;


public abstract class RDFField implements Comparable<RDFField>
{
    public static enum FieldType { Literal, IRI };
    
    public String name;
    public String value;
    
    
    public RDFField(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    
    public abstract FieldType getFieldType();
    
    
    @Override
    public int compareTo(RDFField other)
    {
        return name.compareTo(other.name);
    }

}
