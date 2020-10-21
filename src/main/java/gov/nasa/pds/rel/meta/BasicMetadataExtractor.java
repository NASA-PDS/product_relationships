package gov.nasa.pds.rel.meta;


import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import gov.nasa.pds.rel.util.PdsStringUtils;
import gov.nasa.pds.rel.util.xml.XPathUtils;



public class BasicMetadataExtractor
{
    private XPathExpression xLid;
    private XPathExpression xVid;
    private XPathExpression xTitle;
    private XPathExpression xProdClass;
    

    public BasicMetadataExtractor() throws Exception
    {
        XPathFactory xpf = XPathFactory.newInstance();
        
        xLid = XPathUtils.compileXPath(xpf, "//Identification_Area/logical_identifier");
        xVid = XPathUtils.compileXPath(xpf, "//Identification_Area/version_id");
        xTitle = XPathUtils.compileXPath(xpf, "//Identification_Area/title");
        xProdClass = XPathUtils.compileXPath(xpf, "//Identification_Area/product_class");
    }

    
    public Metadata extract(Document doc) throws Exception
    {
        Metadata md = new Metadata();        
        
        // Basic info
        md.lid = PdsStringUtils.trim(XPathUtils.getStringValue(doc, xLid));
        md.vid = PdsStringUtils.trim(XPathUtils.getStringValue(doc, xVid));
        md.title = StringUtils.normalizeSpace(XPathUtils.getStringValue(doc, xTitle));
        md.prodClass = PdsStringUtils.trim(XPathUtils.getStringValue(doc, xProdClass));

        return md;
    }
    
}
