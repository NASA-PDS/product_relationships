package gov.nasa.pds.rel.out;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import gov.nasa.pds.rel.util.RDFTurtleUtils;


public class RDFTurtleWriter implements Closeable
{
    private Writer writer;
    private boolean firstField = true;
    
    
    public RDFTurtleWriter(File file) throws Exception
    {
        this.writer = new FileWriter(file);
        writeHeader();
    }

    
    public RDFTurtleWriter(Writer writer) throws Exception
    {
        this.writer = writer;
        writeHeader();
    }

    
    private void writeHeader() throws Exception
    {
        // Header
        writer.write("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n");
        writer.write("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n");
        writer.write("PREFIX pds: <pds:>\n");
        writer.write("\n");
    }

    
    @Override
    public void close() throws IOException
    {
        writer.close();
    }
    
    
    public void startRecord(String id) throws Exception
    {
        firstField = true;
        writer.write("<" + id + ">\n");
    }

    
    public void endRecord() throws Exception
    {
        writer.write(".\n");        
    }
    

    private void handleFirstField() throws Exception
    {
        if(!firstField)
        {
            writer.write(";\n");
        }
        
        firstField = false;
    }
    
    
    public void writeIRI(String name, String value) throws Exception
    {
        if(value == null) return;
        handleFirstField();
        
        writer.write("  ");
        writer.write(name);
        writer.write(" ");
        writer.write(value);
    }
    
    
    public void writeLiteral(String name, String value) throws Exception
    {
        writeLiteral(name, value, null);
    }
    
    
    public void writeLiteral(String name, String value, String dataType) throws Exception
    {
        if(value == null) return;
        handleFirstField();
        
        // Predicate
        writer.write("  ");
        writer.write(name);
        writer.write(" ");
        
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

}
