package gov.nasa.pds.rel.util.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XPathUtils
{
    private XPathFactory xpf;
    
    public XPathUtils()
    {
        xpf = XPathFactory.newInstance();
    }
    
    
    public static XPathExpression compileXPath(XPathFactory xpf, String str) throws Exception
    {
        XPath xpath = xpf.newXPath();
        XPathExpression expr = xpath.compile(str);
        return expr;
    }

    
    public static String getStringValue(Document doc, XPathExpression expr) throws Exception
    {
        Object res = expr.evaluate(doc, XPathConstants.STRING);
        return (res == null) ? null : res.toString();
    }

    
    public static List<String> getStringList(Document doc, XPathExpression expr) throws Exception
    {
        String[] values = getStringArray(doc, expr);
        return values == null ? null : Arrays.asList(values);
    }

    
    public List<String> getStringList(Document doc, String xpath) throws Exception
    {
        XPathExpression expr = compileXPath(xpf, xpath);
        return getStringList(doc, expr);
    }

    
    public Set<String> getStringSet(Document doc, String xpath) throws Exception
    {
        XPathExpression expr = compileXPath(xpf, xpath);
        List<String> list = getStringList(doc, expr);
        
        if(list == null || list.size() == 0) return null;

        Set<String> set = new HashSet<>();
        set.addAll(list);
        return set;
    }

    
    public static String[] getStringArray(Document doc, XPathExpression expr) throws Exception
    {
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        if (nodes == null || nodes.getLength() == 0)
            return null;

        String vals[] = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++)
        {
            vals[i] = nodes.item(i).getTextContent();
        }

        return vals;
    }

    
    public static NodeList getNodeList(Object item, XPathExpression expr) throws Exception
    {
        if(item == null) return null;
        
        NodeList nodes = (NodeList)expr.evaluate(item, XPathConstants.NODESET);
        return nodes;
    }
    
    
    public NodeList getNodeList(Object item, String xpath) throws Exception
    {
        if(item == null) return null;
        XPathExpression xpe = compileXPath(xpf, xpath);

        return getNodeList(item, xpe);
    }

    
    public int getNodeCount(Object item, String xpath) throws Exception
    {
        if(item == null) return 0;
        XPathExpression xpe = compileXPath(xpf, xpath);

        NodeList nodes = getNodeList(item, xpe);
        return nodes == null ? 0 : nodes.getLength();
    }
    
    
    public Node getFirstNode(Object item, String xpath) throws Exception
    {
        if(item == null) return null;
        
        XPathExpression xpe = XPathUtils.compileXPath(xpf, xpath);
        NodeList nodes = XPathUtils.getNodeList(item, xpe);
        
        if(nodes == null || nodes.getLength() == 0) 
        {
            return null;
        }
        else
        {
            return nodes.item(0);
        }
    }
    
}
