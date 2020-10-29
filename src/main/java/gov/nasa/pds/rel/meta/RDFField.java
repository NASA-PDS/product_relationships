package gov.nasa.pds.rel.meta;


public class RDFField implements Comparable<RDFField>
{
    public static enum FieldType { Literal, IRI };
    
    public String name;
    public String value;
    public String dataType;
    public FieldType fieldType = FieldType.Literal;
    
    
    public RDFField(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    
    public RDFField(String name, String value, String dataType)
    {
        this(name, value);
        this.dataType = dataType;
    }


    @Override
    public int compareTo(RDFField other)
    {
        return name.compareTo(other.name);
    }

}
