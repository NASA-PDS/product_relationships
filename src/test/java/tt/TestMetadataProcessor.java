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
        testCrawler();
    }

    
    private static void testParser() throws Exception
    {
        //File file = new File("/ws3/OREX/orex_spice/spice_kernels/spk/bennu_refdrmc_v1.xml");
        
        //File file = new File("/ws/data/context/pds4/investigation/mission.orex_1.1.xml");
        //File file = new File("/ws/data/context/pds4/investigation/mission.2001_mars_odyssey_1.0.xml");

        //File file = new File("/ws/data/context/pds4/instrument/orex.ocams_1.0.xml");
        File file = new File("/ws/data/context/pds4/instrument/soho.swan_1.0.xml");
        
        //File file = new File("/ws/data/context/pds4/instrument_host/spacecraft.hst_1.1.xml");
        
        //File file = new File("/ws/data/context/pds4/target/dwarf_planet.136199_eris_1.1.xml");
        //File file = new File("/ws/data/context/pds4/target/asteroid.25143_itokawa_1.0.xml");
        
        MetadataWriter writer = new MetadataWriter(new File("/tmp/test.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        parser.parse(file);
        writer.close();
    }
    
    
    private static void testCrawler() throws Exception
    {
        MetadataWriter writer = new MetadataWriter(new File("/tmp/target.ttl"));
        MetadataProcessor proc = new MetadataProcessor(writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        FileCrawler crawler = new FileCrawler();
        crawler.crawl("/ws/data/context/pds4/target", (file) -> { parser.parse(file); });
        
        writer.close();
    }
}
