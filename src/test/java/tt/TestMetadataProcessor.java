package tt;

import java.io.File;

import gov.nasa.pds.rel.meta.PdsLabelParser;
import gov.nasa.pds.rel.meta.proc.TargetProcessor;
import gov.nasa.pds.rel.out.MetadataWriter;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.cfg.ConfigReader;
import gov.nasa.pds.rel.cfg.Configuration;
import gov.nasa.pds.rel.meta.MetadataProcessor;


public class TestMetadataProcessor
{
    public static void main(String[] args) throws Exception
    {
        //System.out.println(TargetProcessor.extractCometName("1P/Halley"));
        //System.out.println(TargetProcessor.extractCometName("C/1996 B2 (Hyakutake)"));
        
        testParser();
        //testCrawler();
    }

    
    private static void testParser() throws Exception
    {
        //File file = new File("/tmp/d1/UVS_CAL_0016o_0000.XML");
        File file = new File("/tmp/d1/ref.xml");
       
        //File file = new File("/ws3/Geo/lunar/urn-nasa-pds-kaguya_grs_spectra/data_ephemerides/ephemerides_data_collection_inventory.xml");
        
        MetadataWriter writer = new MetadataWriter(new File("/tmp/test.ttl"));
        MetadataProcessor proc = new MetadataProcessor(new Configuration(), writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        parser.parse(file);
        writer.close();
    }
    
    
    private static void testCrawler() throws Exception
    {
        //ConfigReader cfgReader = new ConfigReader();
        //Configuration cfg = cfgReader.read(new File("/tmp/target.cfg"));
        
        MetadataWriter writer = new MetadataWriter(new File("/tmp/target.ttl"));
        MetadataProcessor proc = new MetadataProcessor(new Configuration(), writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/ws/data/context/pds4/target", (file) -> { parser.parse(file); });
        
        writer.close();
    }
    

    private static void testCrawler2() throws Exception
    {
        Configuration cfg = new Configuration();
        cfg.prodFilterExclude.add("product_context");
        
        MetadataWriter writer = new MetadataWriter(new File("/tmp/test.ttl"));
        MetadataProcessor proc = new MetadataProcessor(cfg, writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        FileCrawler crawler = new FileCrawler();
        //crawler.crawl("/ws3/Geo/lunar/urn-nasa-pds-kaguya_grs_spectra/", (file) -> { parser.parse(file); });
        crawler.crawl("/ws3/OREX/orex_spice/", (file) -> { parser.parse(file); });
        
        writer.close();
    }
}
