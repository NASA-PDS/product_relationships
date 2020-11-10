package tt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.pds.rel.meta.proc.TargetProcessor;

public class TestPatterns
{

    public static void main(String[] args)
    {
        /*
        Pattern pattern = Pattern.compile("([A-Za-z]+)");
        Matcher matcher = pattern.matcher("12379 rb9");
        if(matcher.find())
        {
            System.out.println(matcher.start() + " - "+ matcher.end() + " - " + matcher.group(1));
        }
        */
        
        System.out.println(TargetProcessor.extractAsteroidName("4015 Wilson-Harrington"));
    }

}
