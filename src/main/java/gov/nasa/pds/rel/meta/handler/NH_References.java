package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class NH_References implements NodeHandler
{
    private static final String LID_REF = "pds:lid_ref";
    private static final String LIDVID_REF = "pds:lidvid_ref";
    
    
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("Internal_Reference".equals(name.className))
        {
            processInternalRef(node, name, meta);            
        }
        else if("Bundle_Member_Entry".equals(name.className))
        {
            processBundleMemberEntry(node, name, meta);
        }
    }
    
    
    private void processInternalRef(Node node, NameInfo name, Metadata meta)
    {
        if("lid_reference".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addIRIField(LID_REF, "<" + value + ">");
        }
        else if("lidvid_reference".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            
            // Convert to LID
            if("Product_Context".equals(meta.rootElement))
            {
                int idx = value.lastIndexOf("::");
                value = value.substring(0, idx);
                meta.addIRIField(LID_REF, "<" + value + ">");
            }
            else
            {
                meta.addIRIField(LIDVID_REF, "<" + value + ">");
            }
        }
    }
    
    
    private void processBundleMemberEntry(Node node, NameInfo name, Metadata meta)
    {
        if("lid_reference".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addIRIField(LID_REF, "<" + value + ">");
        }
        else if("lidvid_reference".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addIRIField(LIDVID_REF, "<" + value + ">");
        }
    }
}
