package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFField;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.proc.KeywordProcessor;

public class BundleAndCollectionHandler implements NodeHandler
{

    @Override
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("Bundle".equals(name.className))
        {
            processBundleAttributes(node, name, meta);            
        }
        else if("Collection".equals(name.className))
        {
            processCollectionAttributes(node, name, meta);            
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
            meta.addField(new RDFField("pds:description", value));
        }
        else if("keyword".equals(name.attrName))
        {
            String value = node.getTextContent();
            KeywordProcessor.getInstance().addKeywords(value, meta.keywords);
        }
    }


    private void processBundleAttributes(Node node, NameInfo name, Metadata meta)
    {
        if("bundle_type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
    }
    
    
    private void processCollectionAttributes(Node node, NameInfo name, Metadata meta)
    {
        if("collection_type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
    }

}
