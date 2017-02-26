# Goko [![Build Status](https://travis-ci.org/cncgoko/Goko.svg?branch=master)](https://travis-ci.org/cncgoko/Goko) [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://http://discuss.goko.fr/)

Current version  : ```0.3.3```

##Description

Goko is a Java based GCode sender and CNC control software. It can be used to control a controller board based CNC machine. 

Supported controllers :
  * TinyG v0.97 - [doc](https://github.com/synthetos/TinyG/wiki)
  * G2 Core v0.99 - [doc](https://github.com/synthetos/g2/wiki)
  * Grbl v0.9 - [doc](https://github.com/grbl/grbl/wiki)
  * Grbl v0.8c - [doc](https://github.com/grbl/grbl/wiki)

##Getting started
 
See [documentation](http://docs.goko.fr/)

##Report a bug

You can use the [Issues tracker](https://github.com/cncgoko/Goko/issues)

You can also discuss on the forum [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://http://discuss.goko.fr/)

##Changelog - Version 0.3.2 [2017-02-27]

**Added**
 * G2 Core support,
 * Grid minimum and maximum retrieved from TinyG and G2 configuration,
 * MSG display in Digital Read Out for TinyG andd G2 board,

**Fixed**
 * Fixed wrong handling of coordinate system (both Grbl and TinyG) - `Issue #36 <https://github.com/cncgoko/Goko/issues/36>`_ ,
 * Fixed an issue where execution was not displayed in 3d view - `Issue #35 <https://github.com/cncgoko/Goko/issues/35>`_ 
 
