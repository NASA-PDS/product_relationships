package gov.nasa.pds.rel.meta.proc;

import java.util.Set;

public class KeywordProcessor
{
    private static KeywordProcessor instance = new KeywordProcessor();
    
    
    private KeywordProcessor()
    {
    }
    
    
    public static KeywordProcessor getInstance()
    {
        return instance;
    }


    public void addKeywords(String text, Set<String> keywords)
    {
        String[] tokens = text.toLowerCase().split(" ");
        for(String token: tokens)
        {
            if(token.length() > 1) keywords.add(token);
        }
    }
}
