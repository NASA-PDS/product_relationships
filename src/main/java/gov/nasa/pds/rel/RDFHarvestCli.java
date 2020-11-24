package gov.nasa.pds.rel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import gov.nasa.pds.rel.util.Log4jConfigurator;


public class RDFHarvestCli
{
    private Options options;
    private CommandLine cmdLine;
    
    
    public RDFHarvestCli()
    {
        options = new Options();
        initOptions();
    }
    
    
    public void run(String[] args)
    {
        if(args.length == 0)
        {
            printHelp();
            System.exit(1);
        }

        if(!parse(args))
        {
            System.out.println();
            printHelp();
            System.exit(1);
        }

        initLogger();
        
        if(!runCommand())
        {
            System.exit(1);
        }
    }

    
    private boolean runCommand()
    {
        try
        {
            HarvestCmd cmd = new HarvestCmd();
            cmd.run(cmdLine);
            return true;
        }
        catch(Exception ex)
        {
            String msg = ExceptionUtils.getMessage(ex);
            System.out.println("[ERROR] " + msg);
            return false;
        }
    }


    public void printHelp()
    {
        System.out.println("Usage: harvest-rdf <options>");
        System.out.println();
        System.out.println("Required parameters:");
        System.out.println("  -c <file>     Configuration file");
        System.out.println("Optional parameters:");
        System.out.println("  -o <dir>      Output directory. Default is /tmp/harvest-rdf/out");
        System.out.println("  -l <file>     Log file. Default is /tmp/harvest-rdf/harvest.log");
        System.out.println("  -v <level>    Logger verbosity: Debug, Info (default), Warn, Error");
    }

    
    public boolean parse(String[] args)
    {
        try
        {
            CommandLineParser parser = new DefaultParser();
            this.cmdLine = parser.parse(options, args);
            return true;
        }
        catch(ParseException ex)
        {
            System.out.println("[ERROR] " + ex.getMessage());
            return false;
        }
    }

    
    private void initLogger()
    {
        String verbosity = cmdLine.getOptionValue("v", "Info");
        String logFile = cmdLine.getOptionValue("l");

        Log4jConfigurator.configure(verbosity, logFile);
    }

    
    private void initOptions()
    {
        Option.Builder bld;
        
        bld = Option.builder("c").hasArg().argName("file").required();
        options.addOption(bld.build());
        
        bld = Option.builder("o").hasArg().argName("dir");
        options.addOption(bld.build());

        bld = Option.builder("l").hasArg().argName("file");
        options.addOption(bld.build());

        bld = Option.builder("v").hasArg().argName("level");
        options.addOption(bld.build());
    }
    
}
