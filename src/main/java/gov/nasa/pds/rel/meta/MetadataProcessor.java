package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.handler.BundleAndCollectionHandler;
import gov.nasa.pds.rel.meta.handler.ContextAreaHandler;
import gov.nasa.pds.rel.meta.handler.ContextProductHandler;
import gov.nasa.pds.rel.meta.handler.IdentificationAreaHandler;
import gov.nasa.pds.rel.meta.handler.NodeHandler;
import gov.nasa.pds.rel.meta.handler.ReferenceHandler;
import gov.nasa.pds.rel.meta.proc.TargetProcessor;
import gov.nasa.pds.rel.out.MetadataWriter;


public class MetadataProcessor implements PdsLabelParser.Callback
{
    private MetadataWriter writer;
    private Metadata meta;
    
    private Map<String, NodeHandler> nodeHandlers;
    private Map<String, NodeHandler> classHandlers;
    
    private TargetProcessor targetProc;
    private File docFile;
    
    
    public MetadataProcessor(MetadataWriter writer)
    {
        this.writer = writer;

        // Class handlers
        classHandlers = new HashMap<>();
        classHandlers.put("product_context", new ContextProductHandler());        
        
        NodeHandler handler = new BundleAndCollectionHandler();
        classHandlers.put("product_bundle", handler);
        classHandlers.put("product_collection", handler);
        
        // Node handlers
        nodeHandlers = new HashMap<>();
        nodeHandlers.put("Identification_Area", new IdentificationAreaHandler());

        handler = new ContextAreaHandler();
        nodeHandlers.put("Primary_Result_Summary", handler);
        nodeHandlers.put("Science_Facets", handler);
        
        handler = new ReferenceHandler();
        nodeHandlers.put("Internal_Reference", handler);
        nodeHandlers.put("Bundle_Member_Entry", handler);
        
        // Processors
        targetProc = new TargetProcessor();
    }
    
    
    @Override
    public int onDocumentStart(Document doc, File file)
    {
        this.docFile = file;
        meta = new Metadata();
        meta.prodClass = doc.getDocumentElement().getLocalName().toLowerCase();
        
        return CONTINUE;
    }

    
    @Override
    public void onDocumentEnd(Document doc) throws Exception
    {
        processMetadata();
        writer.write(meta);
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

        handler = classHandlers.get(meta.prodClass);
        if(handler != null) 
        {
            handler.onLeafNode(node, name, meta);
            return;
        }
    }

    
    private void processMetadata() throws Exception
    {
        validateMetadata();
        
        if("target".equalsIgnoreCase(meta.prodSubClass))
        {
            targetProc.process(meta);
        }
    }

    
    private void validateMetadata() throws Exception
    {
        if(meta.lid == null) throw new Exception("Missing LID: " + docFile.getAbsolutePath());
        if(meta.vid == null) throw new Exception("Missing VID: " + docFile.getAbsolutePath());
        
        if(meta.type.isEmpty())
        {
            System.out.println("[WARN] Missing 'type': " + meta.lid + "::" + meta.vid);
        }
        
        if("product_context".equals(meta.prodClass))
        {
            if(meta.prodSubClass == null)
            {
                System.out.println("[WARN] Missing 'sub_class': " + meta.lid + "::" + meta.vid);
            }
        }
    }
}
