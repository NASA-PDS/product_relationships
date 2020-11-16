package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFLiteral;
import gov.nasa.pds.rel.meta.proc.KeywordProcessor;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class ContextAreaHandler implements NodeHandler
{

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
    }

    
    private void processPrimaryResultSummary(Node node, NameInfo name, Metadata meta)
    {
        if("purpose".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addField(new RDFLiteral("pds:purpose", value));
        }
        else if("processing_level".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addField(new RDFLiteral("pds:processing_level", value));
        }
        else if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFLiteral("pds:description", value));
        }
    }


    private void processScienceFacets(Node node, NameInfo name, Metadata meta)
    {
        if("domain".equals(name.attrName) || "discipline_name".equals(name.attrName))
        {
            String text = node.getTextContent();
            KeywordProcessor.getInstance().addKeywords(text, meta.keywords);
        }
    }

}
