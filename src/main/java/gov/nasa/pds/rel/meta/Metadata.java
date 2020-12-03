package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


public class Metadata
{
    public File labelFile;
    public String rootElement;
    public String lid;
    public String vid;
    public String title;
    
    protected Map<String, String> tempFields = new TreeMap<>();
    protected Map<String, RDFField> fields = new TreeMap<>();

    
    public Metadata()
    {
    }


    public void addLiteralField(String name, String value)
    {
        addLiteralField(name, value, null);
    }
    
    
    public void addLiteralField(String name, String value, String dataType)
    {
        addField(RDFField.FieldType.Literal, name, value, dataType);
    }

    
    public void addIRIField(String name, String value)
    {
        addField(RDFField.FieldType.IRI, name, value, null);
    }

    
    private void addField(RDFField.FieldType type, String name, String value, String dataType)
    {
        RDFField field = fields.get(name);
        if(field == null)
        {
            field = new RDFField(type, name, value);
            field.dataType = dataType;
            fields.put(name, field);
        }
        else
        {
            field.addValue(value);
        }
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
