package gov.nasa.pds.rel.meta.handler;

import java.time.LocalDate;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFLiteral;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.util.DateUtils;


public class ContextProductHandler implements NodeHandler
{    
    private static final LocalDate DEFAULT_STOP_DATE = LocalDate.of(3000, 1, 1);

    
    public ContextProductHandler()
    {
    }

    
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Investigation".equals(name.className))
        {
            meta.prodClass.add("investigation");
            processCommonAttributes(node, name, meta);
            processInvestigationAttributes(node, name, meta);
        }
        else if("Instrument".equals(name.className))
        {
            meta.prodClass.add("instrument");
            processCommonAttributes(node, name, meta);
        }
        else if("Instrument_Host".equals(name.className))
        {
            meta.prodClass.add("instrument_host");
            processCommonAttributes(node, name, meta);
        }
        else if("Target".equals(name.className))
        {
            meta.prodClass.add("target");
            processCommonAttributes(node, name, meta);
        }
        else if("Type_List".equals(name.className) && "type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
    }


    private void processCommonAttributes(Node node, NameInfo name, Metadata meta)
    {
        if("name".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addTempField("name", value);
        }
        else if("type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
        else if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFLiteral("pds:description", value));
        }        
    }


    private void processInvestigationAttributes(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("start_date".equals(name.attrName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim());
            meta.addField(new RDFLiteral("pds:start_date", value, "xsd:date"));
        }
        else if("stop_date".equals(name.attrName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim(), DEFAULT_STOP_DATE);
            meta.addField(new RDFLiteral("pds:stop_date", value, "xsd:date"));
        }
    }
}
