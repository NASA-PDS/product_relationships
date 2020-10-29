package gov.nasa.pds.rel.out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;
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
        writer.write("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
        writer.write("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
        writer.write("PREFIX pds: <pds:>\n");
        writer.write("\n");
    }


    @Override
    public void write(Metadata meta) throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID");
        if(meta.vid == null) throw new Exception("Missing VID for " + meta.lid);
        if(meta.prodClass == null) throw new Exception("Missing product class for " + meta.lid);
        
        firstField = true;
        //String lidvid = meta.lid + "::" + meta.vid;
        
        writeId(meta.lid);
        
        writeLiteral("pds:product_class", meta.prodClass, null);
        //writeLiteral("pds:lidvid", lidvid);
        writeLiteral("pds:vid", meta.vid, "xsd:float");

        for(RDFField field: meta.fields)
        {
            if(field.fieldType == RDFField.FieldType.Literal)
            {
                writeLiteral(field.name, field.value, field.dataType);
            }
            else
            {
                writeIRI(field.name, field.value);
            }
        }

        // References
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
        writer.write(value);
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
