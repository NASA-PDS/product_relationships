package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class NH_FileArea implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("File".equals(name.className))
        {
            processFile(node, name, meta);            
        }
        else if("Document_File".equals(name.className))
        {
            processFile(node, name, meta);            
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
