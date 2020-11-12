package tt;

import java.io.File;

import gov.nasa.pds.rel.meta.PdsLabelParser;
import gov.nasa.pds.rel.out.MetadataWriter;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.meta.MetadataProcessor;


public class TestMetadataProcessor
{
    public static void main(String[] args) throws Exception
    {
        //testParser();
        testCrawler2();
    }

    
    private static void testParser() throws Exception
    {
        //File file = new File("/ws3/OREX/orex_spice/spice_kernels/spk/bennu_refdrmc_v1.xml");
        //File file = new File("/ws3/OREX/orex_spice/spice_kernels/collection_spice_kernels_v008.xml");
        File file = new File("/ws3/OREX/orex_spice/bundle_orex_spice_v008.xml");
        
        //File file = new File("/ws/data/context/pds4/instrument/soho.swan_1.0.xml");
        
        MetadataWriter writer = new MetadataWriter(new File("/tmp/test.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        parser.parse(file);
        writer.close();
    }
    
    
    private static void testCrawler() throws Exception
    {
        MetadataWriter writer = new MetadataWriter(new File("/tmp/instrument_host.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/ws/data/context/pds4/instrument_host", (file) -> { parser.parse(file); });
        
        writer.close();
    }
    

    private static void testCrawler2() throws Exception
    {
        MetadataWriter writer = new MetadataWriter(new File("/tmp/test.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/ws3/Geo/lunar/urn-nasa-pds-kaguya_grs_spectra/", (file) -> { parser.parse(file); });
        
        writer.close();
    }
}
