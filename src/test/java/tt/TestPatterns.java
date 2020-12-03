package tt;

import gov.nasa.pds.rel.meta.proc.TargetProcessor;


public class TestPatterns
{

    public static void main(String[] args)
    {
        //System.out.println(TargetProcessor.extractAsteroidName("4015 Wilson-Harrington"));
        System.out.println(TargetProcessor.extractCometName("67P/Churyumov-Gerasimenko"));
        System.out.println(TargetProcessor.extractCometName("73P/Schwassmann-Wachmann 3"));
        System.out.println(TargetProcessor.extractCometName("76P/West-Kohoutek-Ikemura"));
    }

}
