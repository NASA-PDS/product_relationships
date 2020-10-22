package gov.nasa.pds.rel.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


public class FileCrawler
{
    public static interface Callback
    {
        public void onFile(File file) throws Exception;
    }

    
    private static class MyFileVisitor extends SimpleFileVisitor<Path>
    {
        private Callback cb;
        private Exception exception;
        
        
        public MyFileVisitor(Callback cb) throws Exception
        {
            if(cb == null) throw new IllegalArgumentException("Callback is null");
            this.cb = cb;
        }
        
        
        public Exception getException()
        {
            return exception;
        }

        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
        {
            if(file.toString().toLowerCase().endsWith(".xml"))
            {
                try
                {
                    cb.onFile(file.toFile());
                }
                catch(Exception ex)
                {
                    this.exception = ex;
                    return FileVisitResult.TERMINATE;
                }
            }
            
            return FileVisitResult.CONTINUE;
        }
    }

    
    public FileCrawler()
    {
    }


    public void crawl(String path, Callback cb) throws Exception
    {
        MyFileVisitor fv = new MyFileVisitor(cb);
        Files.walkFileTree(Paths.get(path), fv);
        
        Exception ex = fv.getException();
        if(ex != null)
        {
            throw ex;
        }
    }
}
