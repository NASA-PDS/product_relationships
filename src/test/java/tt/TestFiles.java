package tt;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import gov.nasa.pds.rel.meta.MetadataProcessor;
import gov.nasa.pds.rel.util.Log4jConfigurator;


public class TestFiles
{
    private static class MyFileVisitor extends SimpleFileVisitor<Path>
    {
        private MetadataProcessor mp;
        
        public MyFileVisitor() throws Exception
        {
            mp = new MetadataProcessor();
        }

        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
        {
            if(file.toString().toLowerCase().endsWith(".xml"))
            {
                try
                {
                    mp.process(file.toFile());
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    return FileVisitResult.TERMINATE;
                }
            }
            
            return FileVisitResult.CONTINUE;
        }
    }
    
    
    public static void main(String[] args) throws Exception
    {
        Log4jConfigurator.configure("INFO", "/tmp/rel.log");
        
        MyFileVisitor fv = new MyFileVisitor();
        Files.walkFileTree(Paths.get("/tmp/d1"), fv);
    }

}
