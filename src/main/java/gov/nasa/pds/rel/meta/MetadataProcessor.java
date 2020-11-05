package gov.nasa.pds.rel.meta;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.handler.IdentificationAreaHandler;
import gov.nasa.pds.rel.meta.handler.InstrumentHandler;
import gov.nasa.pds.rel.meta.handler.InvestigationHandler;
import gov.nasa.pds.rel.meta.handler.NodeHandler;
import gov.nasa.pds.rel.meta.handler.ReferenceHandler;
import gov.nasa.pds.rel.out.MetadataWriter;


public class MetadataProcessor implements PdsLabelParser.Callback
{
    private MetadataWriter writer;
    private Metadata meta;
    
    private Map<String, NodeHandler> nodeHandlers;

    
    public MetadataProcessor(MetadataWriter writer)
    {
        this.writer = writer;
        
        nodeHandlers = new HashMap<>();
        nodeHandlers.put("Identification_Area", new IdentificationAreaHandler());
        nodeHandlers.put("Internal_Reference", new ReferenceHandler());
        nodeHandlers.put("Investigation", new InvestigationHandler());
        nodeHandlers.put("Instrument", new InstrumentHandler());
    }
    
    
    @Override
    public int onDocumentStart(Document doc)
    {
        meta = new Metadata();
        meta.prodClass = doc.getDocumentElement().getLocalName();
        
        return CONTINUE;
    }

    
    @Override
    public void onDocumentEnd(Document doc) throws Exception
    {
        writer.write(meta);
    }

    
    @Override
    public void onLeafNode(Node node, NameInfo name)
    {
        NodeHandler handler = nodeHandlers.get(name.className);
        if(handler != null)
        {
            handler.onLeafNode(node, name, meta);
        }
    }

}
