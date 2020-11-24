package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.cfg.Configuration;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.handler.BundleAndCollectionHandler;
import gov.nasa.pds.rel.meta.handler.ContextAreaHandler;
import gov.nasa.pds.rel.meta.handler.ContextProductHandler;
import gov.nasa.pds.rel.meta.handler.IdentificationAreaHandler;
import gov.nasa.pds.rel.meta.handler.NodeHandler;
import gov.nasa.pds.rel.meta.handler.ReferenceHandler;
import gov.nasa.pds.rel.meta.handler.SpiceKernelHandler;
import gov.nasa.pds.rel.meta.proc.TargetProcessor;
import gov.nasa.pds.rel.out.MetadataWriter;
import gov.nasa.pds.rel.util.CounterMap;


public class MetadataProcessor implements PdsLabelParser.Callback
{
    private Configuration cfg;
    private MetadataWriter writer;
    private Metadata meta;
    
    private Map<String, NodeHandler> nodeHandlers;
    private Map<String, NodeHandler> docTypeHandlers;
    
    private TargetProcessor targetProc;
    private File docFile;
    
    private CounterMap prodCounters = new CounterMap();

    private Logger log;
    
    
    public MetadataProcessor(Configuration cfg, MetadataWriter writer)
    {
        log = LogManager.getLogger(this.getClass());
        
        this.cfg = cfg;
        this.writer = writer;

        // Class handlers
        docTypeHandlers = new HashMap<>();
        docTypeHandlers.put("Product_Context", new ContextProductHandler());        
        
        NodeHandler handler = new BundleAndCollectionHandler();
        docTypeHandlers.put("Product_Bundle", handler);
        docTypeHandlers.put("Product_Collection", handler);
        
        docTypeHandlers.put("Product_SPICE_Kernel", new SpiceKernelHandler());
        
        // Node handlers
        nodeHandlers = new HashMap<>();
        handler = new IdentificationAreaHandler();
        nodeHandlers.put("Identification_Area", handler);
        nodeHandlers.put("Citation_Information", handler);
        
        handler = new ContextAreaHandler();
        nodeHandlers.put("Primary_Result_Summary", handler);
        nodeHandlers.put("Science_Facets", handler);
        
        handler = new ReferenceHandler();
        nodeHandlers.put("Internal_Reference", handler);
        nodeHandlers.put("Bundle_Member_Entry", handler);
        
        // Processors
        targetProc = new TargetProcessor();
    }
    
    
    public CounterMap getProductCounters()
    {
        return prodCounters;
    }
    
    
    @Override
    public int onDocumentStart(Document doc, File file)
    {
        String rootElement = doc.getDocumentElement().getLocalName();
        
        // Apply class filters
        if(cfg.prodFilterInclude != null && cfg.prodFilterInclude.size() > 0)
        {
            if(!cfg.prodFilterInclude.contains(rootElement)) return SKIP;
        }
        if(cfg.prodFilterExclude != null && cfg.prodFilterExclude.size() > 0)
        {
            if(cfg.prodFilterExclude.contains(rootElement)) return SKIP;
        }
        
        log.info("Processing file " + file.toURI().getPath());
        
        this.docFile = file;
        meta = new Metadata();
        meta.rootElement = rootElement;
        if(rootElement.startsWith("Product_"))
        {
            meta.prodClass.add(rootElement.substring(8).toLowerCase());
        }

        return CONTINUE;
    }

    
    @Override
    public void onDocumentEnd(Document doc) throws Exception
    {
        processMetadata();
        writer.write(meta);
        
        prodCounters.inc(meta.rootElement);
    }

    
    @Override
    public void onLeafNode(Node node, NameInfo name) throws Exception
    {
        NodeHandler handler = nodeHandlers.get(name.className);
        if(handler != null) 
        {
            handler.onLeafNode(node, name, meta);
            return;
        }

        handler = docTypeHandlers.get(meta.rootElement);
        if(handler != null) 
        {
            handler.onLeafNode(node, name, meta);
            return;
        }
    }

    
    private void processMetadata() throws Exception
    {
        validateMetadata();
        
        if(meta.prodClass.contains("target"))
        {
            targetProc.process(meta);
        }
    }

    
    private void validateMetadata() throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID: " + docFile.getAbsolutePath());
        if(meta.vid == null) throw new Exception("Missing VID: " + docFile.getAbsolutePath());
        
        if(meta.prodClass.contains("context"))
        {
            if(meta.type.isEmpty())
            {
                log.warn("Missing 'type': " + meta.lid + "::" + meta.vid);
            }
        }
    }
}
