package tt;

import gov.nasa.pds.rel.meta.MetadataProcessor;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.util.Log4jConfigurator;


public class TestFiles
{
    
    public static void main(String[] args) throws Exception
    {
        Log4jConfigurator.configure("INFO", "/tmp/rel.log");

        MetadataProcessor proc = new MetadataProcessor();
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/tmp/d1", (file) -> { proc.process(file); });
    }

}
