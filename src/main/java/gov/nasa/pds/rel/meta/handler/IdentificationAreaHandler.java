package gov.nasa.pds.rel.meta.handler;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class IdentificationAreaHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("pds.Identification_Area.pds.logical_identifier".equals(name.fullName))
        {
            meta.lid = node.getTextContent().trim();
        }
        else if("pds.Identification_Area.pds.version_id".equals(name.fullName))
        {
            meta.vid = node.getTextContent().trim();
        }
        else if("pds.Identification_Area.pds.title".equals(name.fullName))
        {
            meta.title = StringUtils.normalizeSpace(node.getTextContent());
        }
    }
}
