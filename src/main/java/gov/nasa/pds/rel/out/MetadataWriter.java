package gov.nasa.pds.rel.out;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opencsv.CSVReader;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;


public class MetadataWriter implements Closeable
{
    private RDFTurtleWriter writer;
    private Logger log;
    
    
    public MetadataWriter(File file) throws Exception
    {
        this.writer = new RDFTurtleWriter(file);
        log = LogManager.getLogger(this.getClass());
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
        
        // File info
        writeFileInfo(meta);
        
        // Collection inventory
        if(meta.rootElement.equals("Product_Collection"))
        {
            writeCollectionInventory(meta);
        }
        
        writer.endRecord();
    }

    
    @Override
    public void close() throws IOException
    {
        writer.close();
    }

    
    private void writeFileInfo(Metadata meta)
    {
        //String filePath = file.toURI().getPath();
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
    
    
    private void writeCollectionInventory(Metadata meta) throws Exception
    {
        if(meta.inventoryFileName == null)
        {
            log.warn("Collection label is missing inventory file: " + meta.getLabelPath());
        }
        
        File labelDir = meta.getLabelDir();
        File inventoryFile = new File(labelDir, meta.inventoryFileName);
        
        CSVReader rd = new CSVReader(new FileReader(inventoryFile));
        
        String[] values = null;
        while((values = rd.readNext()) != null)
        {
            if(values.length < 2) continue;
            if(values[0].equalsIgnoreCase("P"))
            {
                String ref = values[1];
                String refFieldName = ref.contains("::") ? "pds:lidvid_ref" : "pds:lid_ref";  
                writer.writeIRI(refFieldName, "<" + ref + ">");
            }
        }
        
        rd.close();
    }
}
