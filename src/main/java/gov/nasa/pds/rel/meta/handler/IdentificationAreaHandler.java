package gov.nasa.pds.rel.meta.handler;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFLiteral;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.proc.KeywordProcessor;


public class IdentificationAreaHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("Identification_Area".equals(name.className))
        {
            processIdentificationArea(node, name, meta);            
        }
        else if("Citation_Information".equals(name.className))
        {
            processCitationInformation(node, name, meta);            
        }
    }

    
    private void processCitationInformation(Node node, NameInfo name, Metadata meta)
    {
        if("description".equals(name.attrName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFLiteral("pds:description", value));
        }
        else if("keyword".equals(name.attrName))
        {
            String value = node.getTextContent();
            KeywordProcessor.getInstance().addKeywords(value, meta.keywords);
        }
    }


    private void processIdentificationArea(Node node, NameInfo name, Metadata meta)
    {
        if("logical_identifier".equals(name.attrName))
        {
            meta.lid = node.getTextContent().trim();
        }
        else if("version_id".equals(name.attrName))
        {
            meta.vid = node.getTextContent().trim();
        }
        else if("title".equals(name.attrName))
        {
            meta.title = StringUtils.normalizeSpace(node.getTextContent());
        }
    }
        
}
