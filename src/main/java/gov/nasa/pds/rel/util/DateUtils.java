package gov.nasa.pds.rel.util;

import java.time.LocalDate;

public class DateUtils
{
    public static String normalizeDate(String str) throws Exception
    {
        return normalizeDate(str, null);
    }

    
    public static String normalizeDate(String str, LocalDate defaultDate) throws Exception
    {
        if(str == null || str.isEmpty())
        {
            if(defaultDate == null) throw new Exception("Date is empty");
            return defaultDate.toString();
        }
        
        int year;
        byte month = 1;
        byte day = 1;

        // Year
        int yidx = str.indexOf('-', 1);
        if(yidx < 0)
        {
            year = Integer.parseInt(str);
            return LocalDate.of(year, month, day).toString();
        }
        else
        {
            year = Integer.parseInt(str.substring(0, yidx));
        }

        // Month
        int midx = str.indexOf('-', yidx + 1);
        if(midx < 0)
        {
            month = Byte.parseByte(str.substring(yidx + 1));
            return LocalDate.of(year, month, day).toString();
        }
        else
        {
            month = Byte.parseByte(str.substring(yidx + 1, midx));
        }

        // Day
        int didx = str.indexOf('-', midx + 1);
        if(didx < 0)
        {
            day = Byte.parseByte(str.substring(midx + 1));
            return LocalDate.of(year, month, day).toString();
        }

        throw new Exception("Invalid date '" + str + "'");
    }

}
