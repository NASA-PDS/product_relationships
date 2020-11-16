package gov.nasa.pds.rel.meta;


public class RDFIri extends RDFField
{
    public RDFIri(String name, String value)
    {
        super(name, value);
    }
    

    @Override
    public FieldType getFieldType()
    {
        return FieldType.IRI;
    }

}
