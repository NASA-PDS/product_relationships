package gov.nasa.pds.rel.out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.util.FieldMapSet;
import gov.nasa.pds.rel.util.RDFTurtleUtils;


public class RDFTurtleWriter implements MetadataWriter
{
    private FileWriter writer;
    
    
    public RDFTurtleWriter(File file) throws Exception
    {
        writer = new FileWriter(file);
    }


    @Override
    public void write(Metadata meta) throws Exception
    {
        String lidvid = meta.lid + "::" + meta.vid;
        writer.write("<" + lidvid + ">\n");
        
        // Basic info
        writeField("pds:lidvid", lidvid, true);
        writeField("pds:lid", meta.lid);
        writeField("pds:vid", meta.vid);
        writeField("pds:title", meta.title);
        writeField("pds:product_class", meta.prodClass);

        writer.write(".\n");        
    }


    @Override
    public void close() throws IOException
    {
        writer.close();
    }
    
    
    private void writeField(String name, String value) throws Exception
    {
        writeField(name, value, false);
    }
    
    
    private void writeField(String name, String value, boolean first) throws Exception
    {
        if(value == null) return;
        
        if(!first)
        {
            writer.write(";\n");
        }

        // Predicate
        writer.write("  <" + name + "> ");
        
        // Object
        writer.write("\"");
        RDFTurtleUtils.escapeString(writer, value);
        writer.write("\"");
    }

    
    private void write(FieldMapSet fmap) throws Exception
    {
        if(fmap == null || fmap.isEmpty()) return;
        
        for(String key: fmap.getNames())
        {
            Set<String> values = fmap.getValues(key);
            for(String value: values)
            {
                writeField(key, value);
            }
        }
    }

}
