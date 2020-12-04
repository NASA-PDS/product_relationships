package gov.nasa.pds.rel.meta.handler;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.util.DateUtils;
import gov.nasa.pds.rel.util.xml.XmlDomUtils;


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
        else if("Facility".equals(name.className))
        {
            meta.addLiteralField("pds:class", "facility");
            processCommonAttributes(node, name, meta);
            processFacilityAttributes(node, name, meta);
        }        
        else if("Telescope".equals(name.className))
        {
            meta.addLiteralField("pds:class", "telescope");
            processCommonAttributes(node, name, meta);
            processTelescopeAttributes(node, name, meta);
        }
        else if("Airborne".equals(name.className))
        {
            meta.addLiteralField("pds:class", "airborne");
            processCommonAttributes(node, name, meta);
        }        
    }


    private void processCommonAttributes(Node node, NameInfo name, Metadata meta)
    {
        if("type".equals(name.attrName))
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


    private void processFacilityAttributes(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("address".equals(name.attrName))
        {
            String value = StringUtils.normalizeSpace(node.getTextContent());
            meta.addLiteralField("pds:address", value);
        }
        else if("country".equals(name.attrName))
        {
            String value = node.getTextContent().trim().toLowerCase();
            meta.addLiteralField("pds:country", value);
        }
    }

    
    private void processTelescopeAttributes(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("aperture".equals(name.attrName))
        {
            addValueAndUnit(meta, node, "pds:aperture");
        }
        else if("telescope_longitude".equals(name.attrName))
        {
            addValueAndUnit(meta, node, "pds:longitude");
        }
        else if("telescope_latitude".equals(name.attrName))
        {
            addValueAndUnit(meta, node, "pds:latitude");
        }
        else if("telescope_altitude".equals(name.attrName))
        {
            addValueAndUnit(meta, node, "pds:altitude");
        }
        else if("coordinate_source".equals(name.attrName))
        {
            String value = StringUtils.normalizeSpace(node.getTextContent());
            meta.addLiteralField("pds:coordinate_source", value);
        }
    }

    
    private void addValueAndUnit(Metadata meta, Node node, String fieldName)
    {
        String value = node.getTextContent().trim();
        meta.addLiteralField(fieldName, value, "xsd:float");
        
        String unit = XmlDomUtils.getAttribute(node, "unit");
        if(unit != null)
        {
            meta.addLiteralField(fieldName + "_unit", unit);
        }
    }
}
