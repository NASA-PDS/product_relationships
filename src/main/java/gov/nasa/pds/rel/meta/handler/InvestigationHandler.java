package gov.nasa.pds.rel.meta.handler;

import java.time.LocalDate;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.RDFField;
import gov.nasa.pds.rel.util.DateUtils;


public class InvestigationHandler implements NodeHandler
{
    private static final LocalDate DEFAULT_STOP_DATE = LocalDate.of(3000, 1, 1);
    
    
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
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
        else if("pds.Investigation.pds.description".equals(name.fullName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFField("pds:description", value));
        }
        else if("pds.Investigation.pds.start_date".equals(name.fullName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim());
            meta.addField(new RDFField("pds:start_date", value, "xsd:date"));
        }
        else if("pds.Investigation.pds.stop_date".equals(name.fullName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim(), DEFAULT_STOP_DATE);
            meta.addField(new RDFField("pds:stop_date", value, "xsd:date"));
        }
    }
}
