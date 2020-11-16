package gov.nasa.pds.rel.meta;


public class RDFLiteral extends RDFField
{
    public String dataType;

    
    public RDFLiteral(String name, String value)
    {
        super(name, value);
    }
    
    
    public RDFLiteral(String name, String value, String dataType)
    {
        super(name, value);
        this.dataType = dataType;
    }


    @Override
    public FieldType getFieldType()
    {
        return FieldType.Literal;
    }

}
