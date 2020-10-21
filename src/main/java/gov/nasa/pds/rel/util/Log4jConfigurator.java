package gov.nasa.pds.rel.util;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;


public class Log4jConfigurator
{
    public static void configure(String verbosity, String filePath) 
    {
        ConfigurationBuilder<BuiltConfiguration> cfg = ConfigurationBuilderFactory.newConfigurationBuilder();
        cfg.setStatusLevel(Level.ERROR);
        cfg.setConfigurationName("Main");
        
        // Appenders
        addConsoleAppender(cfg, "console");
        addFileAppender(cfg, "file", filePath);

        // Root logger
        RootLoggerComponentBuilder rootLog = cfg.newRootLogger(Level.OFF);
        rootLog.add(cfg.newAppenderRef("console"));
        rootLog.add(cfg.newAppenderRef("file"));
        cfg.add(rootLog);
        
        // Default Harvest logger
        Level level = parseLogLevel(verbosity);
        LoggerComponentBuilder defLog = cfg.newLogger("gov.nasa.pds.rel", level);
        cfg.add(defLog);
        
        // Minimal logger
        LoggerComponentBuilder minLog = cfg.newLogger("harvest-min-info", Level.INFO);
        cfg.add(minLog);
        
        // Init Log4j
        Configurator.initialize(cfg.build());
    }
    
    
    private static void addConsoleAppender(ConfigurationBuilder<BuiltConfiguration> cfg, String name)
    {
        AppenderComponentBuilder appender = cfg.newAppender(name, "CONSOLE");
        appender.addAttribute("target", ConsoleAppender.Target.SYSTEM_OUT);
        appender.add(cfg.newLayout("PatternLayout").addAttribute("pattern", "[%level] %msg%n%throwable"));
        cfg.add(appender);
    }
    
    
    private static void addFileAppender(ConfigurationBuilder<BuiltConfiguration> cfg, String name, String filePath)
    {
        // Use default log name if not provided
        if(filePath == null)
        {
            File dir = new File("/tmp/harvest");
            dir.mkdirs();
            filePath = "/tmp/harvest/harvest.log";
        }
        
        AppenderComponentBuilder appender = cfg.newAppender(name, "FILE");
        appender.addAttribute("fileName", filePath);
        appender.addAttribute("append", false);
        appender.add(cfg.newLayout("PatternLayout").addAttribute("pattern", "%d [%level] %msg%n%throwable"));
        cfg.add(appender);
    }
    
    
    private static Level parseLogLevel(String verbosity)
    {
        // Logger is not setup yet. Print to console.
        if(verbosity == null)
        {
            System.out.println("[WARN] Log verbosity is not set. Will use 'INFO'.");
            return Level.INFO;
        }
        
        switch(verbosity.toUpperCase())
        {
        case "ALL": return Level.ALL;
        case "INFO": return Level.INFO;
        case "WARN": return Level.WARN;
        case "ERROR": return Level.ERROR;
        }

        // Logger is not setup yet. Print to console.
        System.out.println("[WARN] Invalid log verbosity '" + verbosity + "'. Will use 'INFO'.");
        return Level.INFO;
    }

}
