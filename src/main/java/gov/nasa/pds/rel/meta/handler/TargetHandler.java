package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.RDFField;


public class TargetHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("Target".equals(name.className))
        {
            meta.prodSubClass = "target";
        }

        if("pds.Target.pds.name".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.addTempField("name", value);
        }
        else if("pds.Target.pds.type".equals(name.fullName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
        else if("pds.Target.pds.description".equals(name.fullName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFField("pds:description", value));
        }
    }
}
