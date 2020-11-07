package gov.nasa.pds.rel.meta.handler;

import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.Metadata;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.RDFField;


public class InstrumentHostHandler implements NodeHandler
{
    public void onLeafNode(Node node, NameInfo name, Metadata meta)
    {
        if("pds.Instrument_Host.pds.name".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.addField(new RDFField("pds:name", value));
        }
        else if("pds.Instrument_Host.pds.type".equals(name.fullName))
        {
            String value = node.getTextContent().trim();
            meta.type.add(value);
            meta.prodSubClass = "Instrument_Host";
        }
        else if("pds.Instrument_Host.pds.description".equals(name.fullName))
        {
            String value = node.getTextContent();
            meta.addField(new RDFField("pds:description", value));
        }
    }
}
