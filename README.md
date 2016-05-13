# CompactConfig [![Build Status](http://ci.ketrwu.de/buildStatus/icon?job=CompactConfig)](http://ci.ketrwu.de/job/CompactConfig/)
CompactConfig is a smart configurations system for Java. You can create and handle multiple configurations.

Examples
==========
You can find a simple example [here](https://github.com/KennethWussmann/CompactConfig/tree/master/examples)!

Features
==========
* Save and get:
  - Strings
  - Booleans
  - Integers
  - Longs
  - Floats
  - Doubles
  - List\<String\>
  - List\<Integer\>
* Handle more than one config with the MultiConfigHandler
* Easy to understand for you and your users
* Comments in your Configuration
 
Getting started
================
Just download the lastest version as a JAR from [here](https://github.com/KennethWussmann/CompactConfig/releases/download/v1.2.0/CompactConfig_v1.2.0.jar) and add it to your Build-Path or [download the source code](https://github.com/KennethWussmann/CompactConfig/archive/v1.2.0.zip) and import it.
If you did done this, check out the examples [here](https://github.com/KennethWussmann/CompactConfig/tree/master/examples).
And if you got some questions let me know them [here](mailto:support@ketrwu.de).

You can also use Maven to depend on CompactConfig:
```XML
<dependencies>
    <dependency>
      <groupId>de.ketrwu</groupId>
      <artifactId>compactconfig</artifactId>
      <version>1.2.2-SNAPSHOT</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
      <id>ketrwu-repo</id>
      <url>http://repo.ketrwu.de/repository/maven-snapshots</url>
    </repository>
</repositories>
```
