package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class ReferenceHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("pds.Internal_Reference.pds.lid_reference".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.lidRefs.add("<" + value + ">");
        }
        else if("pds.Internal_Reference.pds.lidvid_reference".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            int idx = value.lastIndexOf("::");
            value = value.substring(0, idx);
            meta.lidRefs.add("<" + value + ">");
        }
    }
}
