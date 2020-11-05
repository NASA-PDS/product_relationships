package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.RDFField;


public class InvestigationHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("pds.Investigation.pds.name".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.addField(new RDFField("pds:name", value));
        }
        else if("pds.Investigation.pds.type".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.type.add(value);
            meta.prodSubClass = "Investigation";
        }
        else if("pds.Investigation.pds.start_date".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.addField(new RDFField("pds:start_date", value, "xsd:date"));
        }
        else if("pds.Investigation.pds.stop_date".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            if(value.isEmpty()) 
            {
                value = "3000-01-01";
            }
            meta.addField(new RDFField("pds:stop_date", value, "xsd:date"));
        }
    }
}
