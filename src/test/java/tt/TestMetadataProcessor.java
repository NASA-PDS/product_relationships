package tt;

import java.io.File;

import gov.nasa.pds.rel.meta.MetadataProcessor;
import gov.nasa.pds.rel.out.RDFTurtleWriter;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.util.Log4jConfigurator;


public class TestMetadataProcessor
{
    
    public static void main(String[] args) throws Exception
    {
        Log4jConfigurator.configure("INFO", "/tmp/rel.log");

        RDFTurtleWriter writer = new RDFTurtleWriter(new File("/tmp/rel.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/tmp/d1", (file) -> { proc.process(file); });
        
        writer.close();
    }

}
