package gov.nasa.pds.rel.meta;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MetadataProcessor
{
    // NOTE: Do not use static variable.
    // Logger is initialized programmatically before an instance of this class is created.
    private Logger LOG;
    
    private BasicMetadataExtractor basicExtractor;

    
    public MetadataProcessor() throws Exception
    {
        LOG = LogManager.getLogger(getClass());

        basicExtractor = new BasicMetadataExtractor();
    }


    public void process(File file) throws Exception
    {
        LOG.info("Processing file " + file.toURI().getPath());
    }
}
