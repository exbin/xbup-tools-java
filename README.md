eXtensible Binary Universal Protocol - Tools
============================================

This repository contains experimental tools and application written in Java to work with XBUP data and example files.  

XBUP is binary data protocol and file format for communication, data storage and application interfaces. 

Homepage: http://xbup.exbin.org  

Structure
---------

Project is constructed from multiple repositories.

  * tools - Tool applications split in submodules
  * src - Sources related to building distribution packages
  * resources - Related resource files, like sample files, images, etc.
  * doc - Documentation + related presentations
  * deps - Folder for downloading libraries for dependency resolution
  * gradle - Gradle wrapper

Compiling
---------

Java Development Kit (JDK) version 8 or later is required to build this project.

For project compiling Gradle 6.0 build system is used: http://gradle.org

You can either download and install gradle or use gradlew or gradlew.bat scripts to download separate copy of gradle to perform the project build.

Build commands: "gradle build" and "gradle distZip"

Dependecies are either downloaded or loaded from local maven repository. 

License
-------

Project uses various libraries with specific licenses and some tools are licensed with multiple licenses with exceptions for specific modules to cover license requirements for used libraries.

Primary license: Apache License, Version 2.0 - see LICENSE-2.0.txt
