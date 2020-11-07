package gov.nasa.pds.rel.meta;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import gov.nasa.pds.rel.util.xml.XmlDomUtils;


public class PdsLabelParser
{
    public static class NameInfo
    {
        public String classNs;
        public String className;
        public String attrNs;
        public String attrName;
        public String fullName;
    }
        
    public static interface Callback
    {
        public final int CONTINUE = 0;
        
        public int onDocumentStart(Document doc);
        public void onDocumentEnd(Document doc) throws Exception;
        public void onLeafNode(Node node, NameInfo name) throws Exception;
    }

    /////////////////////////////////////////////////////////////////
    
    private Callback callback;
    private DocumentBuilderFactory dbf;
    
    private Map<String, String> globalNsMap;    
    private Map<String, String> localNsMap;
    //private PdsDateConverter dateConverter;
    
    
    public PdsLabelParser(Callback cb)
    {
        this.callback = cb;
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        globalNsMap = new TreeMap<>();
        globalNsMap.put("http://pds.nasa.gov/pds4/pds/v1", "pds");
    }

    
    public void parse(File file) throws Exception
    {
        Document doc = XmlDomUtils.readXml(dbf, file);
        
        if(callback.onDocumentStart(doc) == Callback.CONTINUE)
        {
            extract(doc);
        }
        
        callback.onDocumentEnd(doc);
    }
    
    
    private void extract(Document doc) throws Exception
    {
        this.localNsMap = XmlDomUtils.getDocNamespaces(doc);
        
        Element root = doc.getDocumentElement();
        processNode(root);
    }


    private void processNode(Node node) throws Exception
    {
        boolean isLeaf = true;
        
        NodeList nl = node.getChildNodes();
        for(int i = 0; i < nl.getLength(); i++)
        {
            Node cn = nl.item(i);
            if(cn.getNodeType() == Node.ELEMENT_NODE)
            {
                isLeaf = false;
                // Process children recursively
                processNode(cn);
            }
        }
        
        // This is a leaf node. Get value.
        if(isLeaf)
        {
            NameInfo nameInfo = createNameInfo(node);
            callback.onLeafNode(node, nameInfo);
        }
    }

    
    private NameInfo createNameInfo(Node node) throws Exception
    {
        NameInfo info = new NameInfo();
        
        // Data dictionary class
        Node parentNode = node.getParentNode();
        if(parentNode != null)
        {
            info.classNs = getNsPrefix(parentNode);
            info.className = parentNode.getLocalName();
        }
        
        // Data dictionary attribute
        info.attrNs = getNsPrefix(node);
        info.attrName = node.getLocalName();

        // Full name
        info.fullName = info.classNs + "." + info.className + "." + info.attrNs + "." + info.attrName;
        
        return info;
    }
    
    
    private String getNsPrefix(Node node) throws Exception
    {
        String nsUri = node.getNamespaceURI();
        
        // Search gloabl map first
        String nsPrefix = globalNsMap.get(nsUri);
        if(nsPrefix != null) return nsPrefix;
        
        // Then local
        nsPrefix = localNsMap.get(nsUri);
        if(nsPrefix != null) return nsPrefix;
        
        throw new Exception("Unknown namespace: " + nsUri 
                + ". Please declare this namespace in the configuration file.");
    }

}
