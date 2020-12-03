package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.util.DateUtils;


public class DH_Document implements NodeHandler
{

    @Override
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Document".equals(name.className))
        {
            processDocument(node, name, meta);            
        }
        else if("Document_File".equals(name.className))
        {
            processFile(node, name, meta);            
        }
    }

    
    private void processDocument(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addLiteralField("pds:description", value);
        }
        if("publication_date".equals(name.attrName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim());
            meta.addLiteralField("pds:publication_date", value, "xsd:date");
        }
    }

    
    private void processFile(Node node, NameInfo name, Metadata meta)
    {
        if("file_name".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addLiteralField("pds:file_name", value);
        }
    }

}
