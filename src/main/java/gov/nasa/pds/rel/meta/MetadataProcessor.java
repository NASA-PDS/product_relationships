package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.handler.DefaultHandler;
import gov.nasa.pds.rel.meta.handler.IdentificationAreaHandler;
import gov.nasa.pds.rel.meta.handler.InstrumentHandler;
import gov.nasa.pds.rel.meta.handler.InstrumentHostHandler;
import gov.nasa.pds.rel.meta.handler.InvestigationHandler;
import gov.nasa.pds.rel.meta.handler.NodeHandler;
import gov.nasa.pds.rel.meta.handler.ReferenceHandler;
import gov.nasa.pds.rel.meta.handler.TargetHandler;
import gov.nasa.pds.rel.meta.proc.TargetProcessor;
import gov.nasa.pds.rel.out.MetadataWriter;


public class MetadataProcessor implements PdsLabelParser.Callback
{
    private MetadataWriter writer;
    private Metadata meta;
    
    private Map<String, NodeHandler> nodeHandlers;
    private DefaultHandler defaultHandler;
    
    private TargetProcessor targetProc;

    private File docFile;
    
    
    public MetadataProcessor(MetadataWriter writer)
    {
        this.writer = writer;

        // Handlers
        defaultHandler = new DefaultHandler();
        
        nodeHandlers = new HashMap<>();
        nodeHandlers.put("Identification_Area", new IdentificationAreaHandler());
        nodeHandlers.put("Internal_Reference", new ReferenceHandler());
        nodeHandlers.put("Investigation", new InvestigationHandler());
        nodeHandlers.put("Instrument", new InstrumentHandler());
        nodeHandlers.put("Instrument_Host", new InstrumentHostHandler());
        nodeHandlers.put("Target", new TargetHandler());
        
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
        if(handler == null) 
        {
            handler = defaultHandler;
        }

        handler.onLeafNode(node, name, meta);
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
