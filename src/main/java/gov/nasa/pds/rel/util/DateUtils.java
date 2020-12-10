package gov.nasa.pds.rel.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class DateUtils
{
    private static DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    
    public static LocalDate parseDate(String str) throws Exception
    {
        return parseDate(str, null);
    }

    
    public static LocalDate parseDate(String str, LocalDate defaultDate) throws Exception
    {
        if(str == null || str.isEmpty())
        {
            if(defaultDate == null) throw new Exception("Date is empty");
            return defaultDate;
        }
        
        if(str.endsWith("Z")) str = str.substring(0, str.length()-1);
                
        int year;
        byte month = 1;
        byte day = 1;

        // Year
        int yidx = str.indexOf('-', 1);
        if(yidx < 0)
        {
            year = Integer.parseInt(str);
            return LocalDate.of(year, month, day);
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
            return LocalDate.of(year, month, day);
        }
        else
        {
            month = Byte.parseByte(str.substring(yidx + 1, midx));
        }

        // Day
        day = Byte.parseByte(str.substring(midx + 1));
        return LocalDate.of(year, month, day);
    }


    public static LocalTime parseTime(String str) throws Exception
    {
        return parseTime(str, null);
    }
    
    
    public static LocalTime parseTime(String str, LocalTime defaultTime) throws Exception
    {
        if(str == null || str.isEmpty())
        {
            if(defaultTime == null) throw new Exception("Time is empty");
            return defaultTime;
        }
        
        if(str.endsWith("Z")) str = str.substring(0, str.length()-1);
                
        byte hour;
        byte minute = 0;
        byte second = 0;

        // Hour
        int hidx = str.indexOf(':', 1);
        if(hidx < 0)
        {
            hour = Byte.parseByte(str);
            return LocalTime.of(hour, minute, second);
        }
        else
        {
            hour = Byte.parseByte(str.substring(0, hidx));
        }

        // Minute
        int midx = str.indexOf(':', hidx + 1);
        if(midx < 0)
        {
            minute = Byte.parseByte(str.substring(hidx + 1));
            return LocalTime.of(hour, minute, second);
        }
        else
        {
            minute = Byte.parseByte(str.substring(hidx + 1, midx));
        }

        // Second
        int sidx = str.indexOf('.', midx + 1);
        if(sidx < 0)
        {
            second = Byte.parseByte(str.substring(midx + 1));
            return LocalTime.of(hour, minute, second);
        }
        
        // Ignore millis / nanos for now
        
        second = Byte.parseByte(str.substring(midx + 1, sidx));
        return LocalTime.of(hour, minute, second);
    }

    
    public static String normalizeDate(String str) throws Exception
    {
        return normalizeDate(str, null);
    }
    
    
    public static String normalizeDate(String str, LocalDate defaultDate) throws Exception
    {
        LocalDate date = parseDate(str, defaultDate);
        return date.toString();
    }
    
    
    public static String normalizeDateTime(String str) throws Exception
    {
        return normalizeDateTime(str, null);
    }
    
    
    public static String normalizeDateTime(String str, LocalDate defaultDate) throws Exception
    {
        
        if(str == null || str.isEmpty())
        {
            if(defaultDate == null) throw new Exception("Date is empty");
            return defaultDate.toString() + "T00:00:00Z";
        }

        int idx = str.indexOf('T');
        
        // Date only
        if(idx < 0)
        {
            LocalDate date = parseDate(str, defaultDate);
            return date + "T00:00:00Z";
        }

        // Date and time
        String strDate = str.substring(0, idx);
        LocalDate date = parseDate(strDate);

        String strTime = str.substring(idx+1);
        LocalTime time = parseTime(strTime);
        
        return date + "T" + TIME_FMT.format(time) + "Z";
    }

}
