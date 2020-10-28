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
    private boolean firstField;
    
    
    public RDFTurtleWriter(File file) throws Exception
    {
        writer = new FileWriter(file);
        
        // Header
        writer.write("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
        writer.write("\n");
    }


    @Override
    public void write(Metadata meta) throws Exception
    {
        firstField = true;
        String lidvid = meta.lid + "::" + meta.vid;
        
        writeId(meta.lid);
        
        // Basic info
        writeIRI("pds:lidvid", lidvid);
        writeLiteral("pds:vid", meta.vid, "xsd:float");

        writeLiteral("pds:product_class", meta.prodClass);
        writeIRI("rdf:type", meta.type);
        
        writeLiteral("pds:title", meta.title);
        
        writeLiteral("pds:name", meta.name);
        writeLiteral("pds:start_date", meta.startDate, "xsd:date");
        writeLiteral("pds:stop_date", meta.stopDate, "xsd:date");

        for(String ref: meta.lidRefs)
        {
            writeIRI("pds:lid_ref", ref);
        }
        
        writer.write(".\n");        
    }


    @Override
    public void close() throws IOException
    {
        writer.close();
    }
    
    
    private void writeId(String id) throws Exception
    {
        writer.write("<" + id + ">\n");
    }


    private void handleFirstField() throws Exception
    {
        if(!firstField)
        {
            writer.write(";\n");
        }
        
        firstField = false;
    }
    
    
    private void writeIRI(String name, String value) throws Exception
    {
        if(value == null) return;
        handleFirstField();
        
        writePredicate(name);
        writer.write("<" + value + ">");
    }
    
    
    private void writeLiteral(String name, String value) throws Exception
    {
        writeLiteral(name, value, null);
    }
    
    
    private void writePredicate(String name) throws Exception
    {
        writer.write("  " + name + " ");
    }
    
    
    private void writeLiteral(String name, String value, String dataType) throws Exception
    {
        if(value == null) return;
        handleFirstField();
        
        // Predicate
        writePredicate(name);
        
        // Literal
        writer.write("\"");
        RDFTurtleUtils.escapeString(writer, value);
        writer.write("\"");
        
        // Data type
        if(dataType != null)
        {
            writer.write("^^" + dataType);
        }
    }

    
    private void write(FieldMapSet fmap) throws Exception
    {
        if(fmap == null || fmap.isEmpty()) return;
        
        for(String key: fmap.getNames())
        {
            Set<String> values = fmap.getValues(key);
            for(String value: values)
            {
                //writeField(key, value);
            }
        }
    }

}
