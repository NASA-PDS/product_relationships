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
        if(meta.lid == null  || meta.lid.isEmpty()) throw new Exception("Missing LID");
        if(meta.vid == null || meta.vid.isEmpty()) throw new Exception("Missing VID");
        String lidvid = meta.lid + "::" + meta.vid;
        
        writer.startRecord(lidvid);

        // LID, VID, Title
        writer.writeIRI("pds:lid", "<" + meta.lid + ">");
        writer.writeLiteral("pds:vid", meta.vid, "xsd:float");
        writer.writeLiteral("pds:title", meta.title);

        // Other fields
        for(RDFField field: meta.getFields())
        {
            writeField(field);
        }
        
        writer.endRecord();
    }

    
    @Override
    public void close() throws IOException
    {
        writer.close();
    }

    
    private void writeField(RDFField field) throws Exception
    {
        if(field.values != null)
        {
            for(String value: field.values)
            {
                if(field.type == RDFField.FieldType.Literal)
                {
                    writer.writeLiteral(field.name, value, field.dataType);
                }
                else
                {
                    writer.writeIRI(field.name, value);
                }
            }
        }
        else
        {
            if(field.type == RDFField.FieldType.Literal)
            {
                writer.writeLiteral(field.name, field.value, field.dataType);
            }
            else
            {
                writer.writeIRI(field.name, field.value);
            }
        }
    }
}
