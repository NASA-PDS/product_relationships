package gov.nasa.pds.rel.cfg;

import java.io.File;
import org.w3c.dom.Document;

import gov.nasa.pds.rel.cfg.model.Configuration;
import gov.nasa.pds.rel.util.xml.XPathUtils;
import gov.nasa.pds.rel.util.xml.XmlDomUtils;


public class ConfigReader
{
    public static Configuration read(File file) throws Exception
    {
        Document doc = XmlDomUtils.readXml(file);
        String rootElement = doc.getDocumentElement().getNodeName();
        if(!"harvest".equals(rootElement))
        {
            throw new Exception("Invalid root element '" + rootElement + "'. Expecting 'harvest'.");
        }
        
        Configuration cfg = parseDirectories(doc);
        cfg.fileInfo = FileInfoReader.parseFileInfo(doc);
        
        return cfg;
    }

    
    private static Configuration parseDirectories(Document doc) throws Exception
    {
        XPathUtils xpu = new XPathUtils();

        int count = xpu.getNodeCount(doc, "/harvest/directories");
        if(count == 0) throw new Exception("Missing required element '/harvest/directories'.");
        if(count > 1) throw new Exception("Could not have more than one '/harvest/directories' element.");
        
        Configuration cfg = new Configuration();                
        cfg.paths = xpu.getStringSet(doc, "/harvest/directories/path");
        if(cfg.paths == null) throw new Exception("Provide at least one '/harvest/directories/path' element.");

        // Product filters
        parseProductFilters(doc, cfg);

        return cfg;
    }

    
    private static void parseProductFilters(Document doc, Configuration cfg) throws Exception
    {
        XPathUtils xpu = new XPathUtils();
        
        cfg.prodFilterInclude = xpu.getStringSet(doc, "/harvest/productFilter/include");
        cfg.prodFilterExclude = xpu.getStringSet(doc, "/harvest/productFilter/exclude");
        
        if(cfg.prodFilterInclude != null && cfg.prodFilterInclude.size() > 0 
                && cfg.prodFilterExclude != null && cfg.prodFilterExclude.size() > 0)
        {
            throw new Exception("<productFilter> could not have both <include> and <exclude> at the same time.");
        }
    }
}
