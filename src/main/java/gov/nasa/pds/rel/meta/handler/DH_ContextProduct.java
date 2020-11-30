package gov.nasa.pds.rel.meta.handler;

import java.time.LocalDate;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.util.DateUtils;


public class DH_ContextProduct implements NodeHandler
{    
    private static final LocalDate DEFAULT_STOP_DATE = LocalDate.of(3000, 1, 1);

    
    public DH_ContextProduct()
    {
    }

    
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Investigation".equals(name.className))
        {
            meta.addLiteralField("pds:class", "investigation");
            processCommonAttributes(node, name, meta);
            processInvestigationAttributes(node, name, meta);
        }
        else if("Instrument".equals(name.className))
        {
            meta.addLiteralField("pds:class", "instrument");
            processCommonAttributes(node, name, meta);
        }
        else if("Instrument_Host".equals(name.className))
        {
            meta.addLiteralField("pds:class", "instrument_host");
            processCommonAttributes(node, name, meta);
        }
        else if("Target".equals(name.className))
        {
            meta.addLiteralField("pds:class", "target");
            processCommonAttributes(node, name, meta);
        }
        else if("Type_List".equals(name.className) && "type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addLiteralField("pds:type", value);
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
            meta.addLiteralField("pds:type", value);
        }
        else if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addLiteralField("pds:description", value);
        }        
    }


    private void processInvestigationAttributes(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("start_date".equals(name.attrName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim());
            meta.addLiteralField("pds:start_date", value, "xsd:date");
        }
        else if("stop_date".equals(name.attrName))
        {
            String value = DateUtils.normalizeDate(node.getTextContent().trim(), DEFAULT_STOP_DATE);
            meta.addLiteralField("pds:stop_date", value, "xsd:date");
        }
    }
}
