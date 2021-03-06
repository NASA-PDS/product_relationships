package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.cfg.model.Configuration;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.handler.DH_BundleCollection;
import gov.nasa.pds.rel.meta.handler.NH_ContextArea;
import gov.nasa.pds.rel.meta.handler.NH_FileArea;
import gov.nasa.pds.rel.meta.handler.DH_ContextProduct;
import gov.nasa.pds.rel.meta.handler.DH_Document;
import gov.nasa.pds.rel.meta.handler.NH_IdentificationArea;
import gov.nasa.pds.rel.meta.handler.NodeHandler;
import gov.nasa.pds.rel.meta.handler.NH_References;
import gov.nasa.pds.rel.meta.handler.DH_SpiceKernel;
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
    
    private FileMetadataExtractor fileMetaExtractor;
    private CounterMap prodCounters = new CounterMap();

    private Logger log;
    
    
    public MetadataProcessor(Configuration cfg, MetadataWriter writer) throws Exception
    {
        log = LogManager.getLogger(this.getClass());
        
        this.fileMetaExtractor = new FileMetadataExtractor(cfg);
        
        this.cfg = cfg;
        this.writer = writer;

        initNodeHandlers();
        initDocHandlers();
        
        // Processors
        targetProc = new TargetProcessor();
    }
    
    
    public CounterMap getProductCounters()
    {
        return prodCounters;
    }
    

    @Override
    public int onDocumentStart(Document doc, File file) throws Exception
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
        
        meta = new Metadata(file, rootElement);
        meta.addLiteralField("pds:product_class", rootElement.toLowerCase());

        fileMetaExtractor.processLabelFile(file, meta);
        
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
    public int onNode(Node node) throws Exception
    {
        return CONTINUE;
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
        
        if(meta.rootElement.equals("Product_Context"))
        {
            RDFField field = meta.getField(Constants.FIELD_CONTEXT_CLASS);
            if(field.containsValue("target"))
            {
                targetProc.process(meta);
            }
        }
    }

    
    private void validateMetadata() throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID: " + meta.getLabelPath());
        if(meta.vid == null) throw new Exception("Missing VID: " + meta.getLabelPath());
        
        if(meta.rootElement.equals("Product_Context"))
        {
            validateProductContext();
        }
    }

    
    private void validateProductContext()
    {
        if(!meta.rootElement.equals("Product_Context")) return;
        
        RDFField fldClass = meta.getField(Constants.FIELD_CONTEXT_CLASS);
        if(fldClass.containsValue("telescope")) return;
        
        if(meta.getField("pds:type") == null)
        {
            log.warn("Missing 'type': " + meta.getLabelPath());
        }
    }
    

    private void initNodeHandlers()
    {
        nodeHandlers = new HashMap<>();
        NodeHandler handler;

        handler = new NH_IdentificationArea();
        nodeHandlers.put("Identification_Area", handler);
        nodeHandlers.put("Citation_Information", handler);
        
        handler = new NH_References();
        nodeHandlers.put("Internal_Reference", handler);
        nodeHandlers.put("Bundle_Member_Entry", handler);

        handler = new NH_ContextArea();
        nodeHandlers.put("Time_Coordinates", handler);
        nodeHandlers.put("Primary_Result_Summary", handler);
        nodeHandlers.put("Science_Facets", handler);
        
        handler = new NH_FileArea(fileMetaExtractor);
        nodeHandlers.put("Document_File", handler);
        nodeHandlers.put("File", handler);
    }


    private void initDocHandlers()
    {
        docTypeHandlers = new HashMap<>();
        NodeHandler handler;
        
        docTypeHandlers.put("Product_Context", new DH_ContextProduct());        
        
        handler = new DH_BundleCollection();
        docTypeHandlers.put("Product_Bundle", handler);
        docTypeHandlers.put("Product_Collection", handler);
        
        docTypeHandlers.put("Product_SPICE_Kernel", new DH_SpiceKernel());
        docTypeHandlers.put("Product_Document", new DH_Document());
    }

}
