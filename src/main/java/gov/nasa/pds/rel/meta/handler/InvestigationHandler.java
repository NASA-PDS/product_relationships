package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class InvestigationHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("pds.Investigation.pds.name".equals(name.fullName))
        {
            meta.name = node.getTextContent().trim();
        }
        else if("pds.Investigation.pds.type".equals(name.fullName))
        {
            meta.type = node.getTextContent().trim();
        }
        else if("pds.Investigation.pds.start_date".equals(name.fullName))
        {
            meta.startDate = node.getTextContent().trim();
        }
        else if("pds.Investigation.pds.stop_date".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.stopDate = value.isEmpty() ? "3000-01-01" : value;
        }
    }
}
