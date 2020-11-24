package gov.nasa.pds.rel;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nasa.pds.rel.cfg.ConfigReader;
import gov.nasa.pds.rel.cfg.Configuration;
import gov.nasa.pds.rel.meta.MetadataProcessor;
import gov.nasa.pds.rel.meta.PdsLabelParser;
import gov.nasa.pds.rel.out.MetadataWriter;
import gov.nasa.pds.rel.util.CounterMap;
import gov.nasa.pds.rel.util.FileCrawler;
import gov.nasa.pds.rel.util.LogUtils;


public class HarvestCmd
{
    private Logger log;
    
    
    public HarvestCmd()
    {
        log = LogManager.getLogger(this.getClass());
    }
    
    
    public void run(CommandLine cmdLine) throws Exception
    {
        // Setup output directory
        File outDir = new File(cmdLine.getOptionValue("o", "/tmp/harvest-rdf/out"));
        log.log(LogUtils.LEVEL_SUMMARY, "Output directory: " + outDir.getAbsolutePath());
        outDir.mkdirs();
                
        // Read config file
        File cfgFile = new File(cmdLine.getOptionValue("c"));
        log.log(LogUtils.LEVEL_SUMMARY, "Reading configuration from " + cfgFile);        
        Configuration cfg = ConfigReader.read(cfgFile);
        
        // Create metadata processor and parser
        MetadataWriter writer = new MetadataWriter(new File(outDir, "data.ttl"));
        MetadataProcessor proc = new MetadataProcessor(cfg, writer);
        PdsLabelParser parser = new PdsLabelParser(proc);
        
        // Crawl directories
        FileCrawler crawler = new FileCrawler();
        for(String path: cfg.paths)
        {
            crawler.crawl(path, (file) -> { parser.parse(file); });
        }
        
        writer.close();

        // Print summary
        printSummary(proc.getProductCounters());
    }
    
    
    private void printSummary(CounterMap counters)
    {
        log.log(LogUtils.LEVEL_SUMMARY, "Summary:");
        int total = counters.getTotal();
        log.log(LogUtils.LEVEL_SUMMARY, "Processed files: " + total);

        if(total > 0)
        {
            log.log(LogUtils.LEVEL_SUMMARY, "File counts by type:");
            for(CounterMap.Item item: counters.getCounts())
            {
                log.log(LogUtils.LEVEL_SUMMARY, "  " + item.name + ": " + item.count);
            }
        }
    }

}
