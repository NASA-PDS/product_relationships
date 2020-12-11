package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.FileMetadataExtractor;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class NH_FileArea implements NodeHandler
{
    private FileMetadataExtractor fileMetaExtractor;
    
    
    public NH_FileArea(FileMetadataExtractor fme)
    {
        this.fileMetaExtractor = fme;
    }
    
    
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("File".equals(name.className))
        {
            processFile(node, name, meta);
        }
        else if("Document_File".equals(name.className))
        {
            processFile(node, name, meta);            
        }
    }

    
    private void processFile(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("file_name".equals(name.attrName))
        {
            String fileName = node.getTextContent().trim();
            if(meta.rootElement.equals("Product_Collection"))
            {
                meta.inventoryFileName = fileName;
            }
            
            fileMetaExtractor.processDataFile(fileName, meta);
        }
    }
    
}
