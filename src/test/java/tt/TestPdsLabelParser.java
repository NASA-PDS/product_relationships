package tt;

import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gov.nasa.pds.rel.meta.PdsLabelParser;
import gov.nasa.pds.rel.meta.PdsLabelParser.NameInfo;
import gov.nasa.pds.rel.meta.Metadata;


public class TestPdsLabelParser
{
    private static class MyCB implements PdsLabelParser.Callback
    {
        private Metadata meta;
        
        public MyCB()
        {
        }

        
        @Override
        public int onDocumentStart(Document doc)
        {
            meta = new Metadata();
            meta.prodClass = doc.getDocumentElement().getLocalName();
            
            return CONTINUE;
        }

        
        @Override
        public void onDocumentEnd(Document doc)
        {
            // Write doc
        }

        
        @Override
        public void onLeafNode(Node node, NameInfo name)
        {
            if("pds.Internal_Reference.pds.lid_reference".equals(name.fullName))
            {
                System.out.println("  <pds:lid_ref> <" + node.getTextContent() + ">;");
            }
            else if("pds.Internal_Reference.pds.lidvid_reference".equals(name.fullName))
            {
                String value = node.getTextContent();
                int idx = value.lastIndexOf("::");
                value = value.substring(0, idx);
                System.out.println("  <pds:lid_ref> <" +  value + ">;");
            }
            
            // Identification_Area.logical_identifier
            // Identification_Area.version_id
            // Identification_Area.title
        }
        
    }
    
    
    public static void main(String[] args) throws Exception
    {
        //File file = new File("/ws3/OREX/orex_spice/spice_kernels/spk/bennu_refdrmc_v1.xml");
        //File file = new File("/ws/data/context/pds4/investigation/mission.orex_1.1.xml");

        File file = new File("/ws/data/context/pds4/investigation/mission.2001_mars_odyssey_1.0.xml");
        
        PdsLabelParser parser = new PdsLabelParser();
        parser.parse(file, new MyCB());
    }

}
