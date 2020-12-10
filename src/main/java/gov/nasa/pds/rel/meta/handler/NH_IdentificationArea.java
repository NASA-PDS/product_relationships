package gov.nasa.pds.rel.meta.handler;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class NH_IdentificationArea implements NodeHandler
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
            meta.addLiteralField("pds:description", value);
        }
        else if("keyword".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.addLiteralField("pds:keyword", value);
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
