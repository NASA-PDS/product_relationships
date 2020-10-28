package gov.nasa.pds.rel.meta;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import gov.nasa.pds.rel.out.MetadataWriter;
import gov.nasa.pds.rel.util.xml.XmlDomUtils;


public class MetadataProcessor
{
    // NOTE: Do not use static variable.
    // Logger is initialized programmatically before an instance of this class is created.
    private Logger LOG;
    
    private PdsLabelParser fieldExtractor;
    private MetadataWriter writer;
    
    
    public MetadataProcessor(MetadataWriter writer) throws Exception
    {
        if(writer == null) throw new IllegalArgumentException("Writer is null");
        this.writer = writer;
        
        LOG = LogManager.getLogger(getClass());

        fieldExtractor = new PdsLabelParser();
    }


    public void process(File file) throws Exception
    {
        LOG.info("Processing file " + file.toURI().getPath());
        
        //fieldExtractor.extract(file, cb);
        
        //writer.write(md);
    }
}
