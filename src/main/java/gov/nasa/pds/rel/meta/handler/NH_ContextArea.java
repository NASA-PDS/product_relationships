package gov.nasa.pds.rel.meta.handler;

import java.time.LocalDate;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.util.DateUtils;


public class NH_ContextArea implements NodeHandler
{
    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1965, 1, 1);
    private static final LocalDate DEFAULT_STOP_DATE = LocalDate.of(3000, 1, 1);
    
    @Override
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Primary_Result_Summary".equals(name.className))
        {
            processPrimaryResultSummary(node, name, meta);
        }
        else if("Science_Facets".equals(name.className))
        {
            processScienceFacets(node, name, meta);
        }
        else if("Time_Coordinates".equals(name.className))
        {
            processTime(node, name, meta);
        }
    }

    
    private void processPrimaryResultSummary(Node node, NameInfo name, Metadata meta)
    {
        if("purpose".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addLiteralField("pds:purpose", value);
        }
        else if("processing_level".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addLiteralField("pds:processing_level", value);
        }
        else if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addLiteralField("pds:description", value);
        }
    }


    private void processScienceFacets(Node node, NameInfo name, Metadata meta)
    {
        String value = node.getTextContent().toLowerCase();
        meta.addLiteralField("pds:science_facets", value);
    }

    
    private void processTime(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("start_date_time".equals(name.attrName))
        {
            String value = DateUtils.normalizeDateTime(node.getTextContent().trim(), DEFAULT_START_DATE);
            meta.addLiteralField("pds:start_date_time", value, "xsd:dateTime");
        }
        else if("stop_date_time".equals(name.attrName))
        {
            String value = DateUtils.normalizeDateTime(node.getTextContent().trim(), DEFAULT_STOP_DATE);
            meta.addLiteralField("pds:stop_date_time", value, "xsd:dateTime");
        }
    }
}
