package tt;

import java.io.File;

import gov.nasa.pds.rel.meta.MetadataProcessor;
import gov.nasa.pds.rel.meta.PdsLabelParser;
import gov.nasa.pds.rel.meta.PdsLabelParserCB;
import gov.nasa.pds.rel.out.RDFTurtleWriter;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.util.Log4jConfigurator;


public class TestMetadataProcessor
{
    
    public static void main(String[] args) throws Exception
    {
        Log4jConfigurator.configure("INFO", "/tmp/rel.log");
        
        testFile();
    }


    public static void testFile() throws Exception
    {
        RDFTurtleWriter writer = new RDFTurtleWriter(new File("/tmp/rel.ttl"));
        PdsLabelParser parser = new PdsLabelParser();
        PdsLabelParserCB cb = new PdsLabelParserCB(writer);
        File file = new File("/ws/data/context/pds4/investigation/mission.2001_mars_odyssey_1.0.xml");
        parser.parse(file, cb);
        writer.close();
    }

    
    public static void testCrawl() throws Exception
    {
        RDFTurtleWriter writer = new RDFTurtleWriter(new File("/tmp/rel.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/tmp/d1", (file) -> { proc.process(file); });
        
        writer.close();
    }
}
