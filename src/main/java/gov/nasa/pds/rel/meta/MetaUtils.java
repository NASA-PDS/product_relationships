package gov.nasa.pds.rel.meta;

public class MetaUtils
{
    public static String normalizeType(String str)
    {
        if(str == null) return null;
        return str.trim().toLowerCase().replaceAll(" ", "_");
    }
}
