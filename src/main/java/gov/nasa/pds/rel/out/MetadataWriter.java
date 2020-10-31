package gov.nasa.pds.rel.out;

import java.io.Closeable;
import java.io.IOException;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;


public class MetadataWriter implements Closeable
{
    private RDFTurtleWriter writer;

    
    public MetadataWriter()
    {
        
    }
    
    
    public void write(Metadata meta) throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID");
        if(meta.vid == null) throw new Exception("Missing VID for " + meta.lid);
        if(meta.prodClass == null) throw new Exception("Missing product class for " + meta.lid);
        
        //String lidvid = meta.lid + "::" + meta.vid;
        
        writer.startRecord(meta.lid);
        
        writer.writeLiteral("pds:product_class", meta.prodClass, null);
        //writeLiteral("pds:lidvid", lidvid);
        writer.writeLiteral("pds:vid", meta.vid, "xsd:float");

        for(RDFField field: meta.fields)
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

        // References
        for(String ref: meta.lidRefs)
        {
            writer.writeIRI("pds:lid_ref", ref);
        }
        
        writer.endRecord();
    }

    
    @Override
    public void close() throws IOException
    {
        writer.close();
    }

}
