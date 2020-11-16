package tt;

import java.io.FileReader;

import com.opencsv.CSVReader;

public class Test1
{

    public static void main(String[] args) throws Exception
    {
        String path = "/tmp/orex/orex_spice/spice_kernels/collection_spice_kernels_inventory_v008.csv";
        CSVReader rd = new CSVReader(new FileReader(path));

        System.out.println("<urn:nasa:pds:orex.spice:spice_kernels::8.0>");
        
        String[] values = null;
        while((values = rd.readNext()) != null)
        {
            if(values[0].equalsIgnoreCase("P"))
            {
                System.out.println("  <pds:lidvid_ref> <" + values[1] + ">;");
            }
        }
        
        rd.close();
    }

}
