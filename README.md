# Goko [![Build Status](https://travis-ci.org/cncgoko/Goko.svg?branch=master)](https://travis-ci.org/cncgoko/Goko) [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://discuss.goko.fr/)

Current version  : ```0.3.3 Development branch```

##Description

Goko is a Java based GCode sender and CNC control software. It can be used to control a controller board based CNC machine. 

Supported controllers :
  * TinyG v0.97 - [doc](https://github.com/synthetos/TinyG/wiki)
  * Grbl v0.9 - [doc](https://github.com/grbl/grbl/wiki)
  * Grbl v0.8c - [doc](https://github.com/grbl/grbl/wiki)

##Getting started
 
See [documentation](http://docs.goko.fr/)

##Report a bug

You can use the [Issues tracker](https://github.com/cncgoko/Goko/issues)

You can also discuss on the forum [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://discuss.goko.fr/)

##Changelog - Version 0.3.2 [2017-01-14]

###Added
 - *Code macro support*,
 - Added Grbl configuration watcher to maintain a stable and comptabile Grbl configuration,
 - Added Serial Console user defined display filter to avoid flooding the console,
 - Added preference to set the default view in 3D viewer at application startup ( in _Viewer_ page),
 - Added Reset action in command panel for both TinyG and Grbl,

###Fixed
 - Fixed error at startup `org.eclipse.swt.SWTException: Failed to execute runnable`,
 - Fixed Grbl not completing execution when Grbl errors happened during streaming,
 - Fixed size of Target board selection dialog not being displayed on high DPI monitors,
 - Fixed rendering of arc motion with rotary axis,
 - Fixed a bug where configured DRO settings were not saved,
 - Fixed a bug where the update confirmation was covered by the Progress dialog,
 - Fixed TinyG controller wrong handling of Inch units

