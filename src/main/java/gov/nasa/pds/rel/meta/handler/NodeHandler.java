package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;


public interface NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta) throws Exception;
}
