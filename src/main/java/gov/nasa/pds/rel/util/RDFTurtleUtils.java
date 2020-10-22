package gov.nasa.pds.rel.util;

import java.io.IOException;

public class RDFTurtleUtils
{

    public static String escapeString(String str) throws Exception
    {
        StringBuilder sb = new StringBuilder(str.length());
        escapeString(sb, str);
        return sb.toString();
    }

    
    public static void escapeString(Appendable appendable, String str) throws IOException
    {
        int strlen = str.length();

        for (int i = 0; i < strlen; i++)
        {
            char c = str.charAt(i);

            if (c == '\\')
            {
                appendable.append("\\\\");
            }
            else if (c == '"')
            {
                appendable.append("\\\"");
            }
            else if (c == '\n')
            {
                appendable.append("\\n");
            }
            else if (c == '\r')
            {
                appendable.append("\\r");
            }
            else if (c == '\t')
            {
                appendable.append("\\t");
            }
            else
            {
                appendable.append(c);
            }
        }
    }

}
