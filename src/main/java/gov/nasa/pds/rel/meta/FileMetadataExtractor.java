package gov.nasa.pds.rel.meta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;

import org.apache.commons.codec.binary.Hex;
import org.apache.tika.Tika;

import gov.nasa.pds.rel.cfg.model.Configuration;
import gov.nasa.pds.rel.cfg.model.FileInfoCfg;
import gov.nasa.pds.rel.util.CloseUtils;


public class FileMetadataExtractor
{
    private FileInfoCfg cfg;
    
    private MessageDigest md5Digest;
    private byte[] buf;
    private Tika tika;

    
    public FileMetadataExtractor(Configuration config) throws Exception
    {
        this.cfg = config.fileInfo;

        if(this.cfg != null)
        {
            md5Digest = MessageDigest.getInstance("MD5");
            buf = new byte[1024 * 16];
            tika = new Tika();
        }
    }

    
    public String getMd5(File file) throws Exception
    {
        md5Digest.reset();
        FileInputStream source = null;
        
        try
        {
            source = new FileInputStream(file);
            
            int count = 0;
            while((count = source.read(buf)) >= 0)
            {
                md5Digest.update(buf, 0, count);
            }
            
            byte[] hash = md5Digest.digest();
            return Hex.encodeHexString(hash);
        }
        finally
        {
            CloseUtils.close(source);
        }
    }
    
    
    public String getBlob(File file) throws Exception
    {
        FileInputStream source = null;
        
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        DeflaterOutputStream dest = new DeflaterOutputStream(bas);
        
        try
        {
            source = new FileInputStream(file);
            
            int count = 0;
            while((count = source.read(buf)) >= 0)
            {
                dest.write(buf, 0, count);
            }
            
            dest.close();
            return Base64.getEncoder().encodeToString(bas.toByteArray());
        }
        finally
        {
            CloseUtils.close(source);
        }
    }


    public String getFileRef(File file)
    {
        String filePath = file.toURI().getPath();
        
        if(cfg != null && cfg.fileRef != null)
        {
            for(FileInfoCfg.FileRefCfg rule: cfg.fileRef)
            {
                if(filePath.startsWith(rule.prefix))
                {
                    filePath = rule.replacement + filePath.substring(rule.prefix.length());
                    break;
                }
            }
        }
        
        return filePath;
    }

    
    public String getMimeType(File file) throws Exception
    {
        InputStream is = null;
        
        try
        {
            is = new FileInputStream(file);
            return tika.detect(is);
        }
        finally
        {
            CloseUtils.close(is);
        }
    }

    
    public void processLabelFile(File file, Metadata meta) throws Exception
    {
        if(cfg == null) return;
        
        meta.addLiteralField("reg:label_file_name", file.getName());
        meta.addLiteralField("reg:label_file_ref", getFileRef(file));
    }
    
    
    public void processDataFile(String fileName, Metadata meta) throws Exception
    {
        if(cfg == null) return;
        
        File file = new File(meta.getLabelDir(), fileName);
        meta.addLiteralField("reg:data_file_name", fileName);
        meta.addLiteralField("reg:data_file_ref", getFileRef(file));

        if(!cfg.processDataFiles) return;
        
        if(!file.exists())
        {
            throw new Exception("Data file " + file.getAbsolutePath() + " doesn't exist");
        }
        
    }
}
