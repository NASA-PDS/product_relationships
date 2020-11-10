package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class DefaultHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Type_List".equals(name.className) && "type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }

    }
}
