# Overview
A command line tool to extract basic metadata and product relationships information from PDS4 label files.
Extracted information is stored in RDF Turtle files.

# Installation
This is a Java Maven project. Run "mvn package" to create binary zip file (target/harvest-rdf-1.0-bin.zip)
Extract generated zip file to any folder (which we will call HARVEST_HOME).

# Basic Operation
Create a configuration file, for example, <i>/tmp/harvest-rdf.xml</i>. 
In this example <i>/ws/data/context/pds4/</i> folder contains PDS4 labels of context products.
```
<?xml version="1.0" encoding="UTF-8"?>

<harvest>
    <directories>
        <path>/ws/data/context/pds4/</path>
    </directories>
</harvest>

```

Run <i>harvest-rdf.bat</i> on Windows or <i>harvest-rdf</i> on Unix, located in <i>HARVEST_HOME/bin</i> folder.
```
harvest-rdf -c /tmp/harvest-rdf.xml
```

The tool will process all PDS4 labels located in <i>/ws/data/context/pds4/</i> folder and its sub-folders.
By default, generated RDF Turtle file is located in <i>/tmp/harvest-rdf/data.ttl</i>.
You can change default output folder by providing "-o" parameter.
```
harvest-rdf.bat -c /tmp/harvest-rdf.xml -o /tmp/rdf1
```

To see basic usage information, run <i>harvest-rdf.bat</i> or <i>harvest-rdf</i> without any parameters.
```
Usage: harvest-rdf <options>

Required parameters:
  -c <file>     Configuration file
Optional parameters:
  -o <dir>      Output directory. Default is /tmp/harvest-rdf/out
  -l <file>     Log file. Default is /tmp/harvest-rdf/harvest.log
  -v <level>    Logger verbosity: Debug, Info (default), Warn, Error
```
