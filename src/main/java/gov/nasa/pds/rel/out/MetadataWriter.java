package gov.nasa.pds.rel.out;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;


public class MetadataWriter implements Closeable
{
    private RDFTurtleWriter writer;

    
    public MetadataWriter(File file) throws Exception
    {
        this.writer = new RDFTurtleWriter(file);
    }
    
    
    public void write(Metadata meta) throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID");
        if(meta.vid == null) throw new Exception("Missing VID for " + meta.lid);
        String lidvid = meta.lid + "::" + meta.vid;
        if(meta.prodClass == null) throw new Exception("Missing product class for " + lidvid);
        
        writer.startRecord(lidvid);

        // LID & VID
        writer.writeIRI("pds:lid", "<" + meta.lid + ">");
        writer.writeLiteral("pds:vid", meta.vid, "xsd:float");

        // Class & Type
        writer.writeLiteral("pds:class", meta.prodClass);
        writer.writeLiteral("pds:sub_class", meta.prodSubClass);
        for(String val: meta.type)
        {
            writer.writeLiteral("pds:type", val);
        }
                
        // References
        for(String ref: meta.lidRefs)
        {
            writer.writeIRI("pds:lid_ref", ref);
        }

        // Other fields
        for(RDFField field: meta.fields.values())
        {
            if(field.fieldType == RDFField.FieldType.Literal)
            {
                writer.writeLiteral(field.name, field.value, field.dataType);
            }
            else
            {
                writer.writeIRI(field.name, field.value);
            }
        }
        
        writer.endRecord();
    }

    
    @Override
    public void close() throws IOException
    {
        writer.close();
    }

}
