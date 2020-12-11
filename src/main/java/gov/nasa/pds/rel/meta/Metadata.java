package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


public class Metadata
{
    private File labelFile;
    private File labelDir;
    
    public String rootElement;
    public String lid;
    public String vid;
    public String title;
    public String inventoryFileName;
    
    protected Map<String, RDFField> fields = new TreeMap<>();

    
    public Metadata(File file, String rootElement)
    {
        this.labelFile = file;
        this.labelDir = labelFile.getParentFile();
        
        this.rootElement = rootElement;
    }


    public File getLabelFile()
    {
        return labelFile;
    }

    
    public File getLabelDir()
    {
        return labelDir;
    }

    
    public String getLabelPath()
    {
        return labelFile.getAbsolutePath();
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
    
}
