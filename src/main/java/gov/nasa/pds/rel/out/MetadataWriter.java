package gov.nasa.pds.rel.out;

import java.io.Closeable;
import gov.nasa.pds.rel.meta.Metadata;


public interface MetadataWriter extends Closeable
{
    public void write(Metadata meta) throws Exception;    
}
