package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.MetaUtils;
import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.RDFLiteral;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public class SpiceKernelHandler implements NodeHandler
{

    @Override
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception
    {
        if("SPICE_Kernel".equals(name.className))
        {
            processSpiceKernel(node, name, meta);            
        }
        else if("File".equals(name.className))
        {
            processFile(node, name, meta);            
        }
    }

    
    private void processSpiceKernel(Node node, NameInfo name, Metadata meta)
    {
        if("kernel_type".equals(name.attrName))
        {
            String value = MetaUtils.normalizeType(node.getTextContent());
            meta.type.add(value);
        }
    }

    
    private void processFile(Node node, NameInfo name, Metadata meta)
    {
        if("file_name".equals(name.attrName))
        {
            String value = node.getTextContent().trim();
            meta.addField(new RDFLiteral("pds:file_name", value));
        }
    }

}
