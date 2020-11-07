package tt;

import gov.nasa.pds.rel.util.DateUtils;

public class TestDateUtils
{

    public static void main(String[] args) throws Exception
    {
        System.out.println(DateUtils.normalizeDate("2010"));
        System.out.println(DateUtils.normalizeDate("2010-10"));
        System.out.println(DateUtils.normalizeDate("20000-1-2"));

        System.out.println(DateUtils.normalizeDate("-2010"));
        System.out.println(DateUtils.normalizeDate("-2010-10"));
        System.out.println(DateUtils.normalizeDate("-20000-1-2"));
        
    }

}
