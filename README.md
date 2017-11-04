# Goko [![Build Status](https://travis-ci.org/cncgoko/Goko.svg?branch=master)](https://travis-ci.org/cncgoko/Goko) [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://discuss.goko.fr/)

Current version  : ```0.3.5```

## Description

Goko is a Java based GCode sender and CNC control software. It can be used to control a controller board based CNC machine. 

Supported controllers :
  * TinyG v0.97 - [doc](https://github.com/synthetos/TinyG/wiki)
  * G2 Core v0.99 - [doc](https://github.com/synthetos/g2/wiki)
  * Grbl v1.1 - [doc](https://github.com/gnea/grbl/wiki)
  * Grbl v0.9 - [doc](https://github.com/grbl/grbl/wiki)
  * Grbl v0.8c - [doc](https://github.com/grbl/grbl/wiki)

## Getting started
 
See [documentation](http://docs.goko.fr/)

## Report a bug

You can use the [Issues tracker](https://github.com/cncgoko/Goko/issues)

You can also discuss on the forum [![Discuss](https://img.shields.io/badge/goko-discuss-blue.svg)](http://discuss.goko.fr/)

## Changelog - Version 0.3.5 [2017-11-04]

**Added**
 * Zero probe tool, allowing to zero using touch probe, including tool offset,
 * Forcing TinyG status report format on connection,
 * Added mouse position coordinate display in 3d viewer,
 * Added Gcode colorization mode allowing to colorize by motion mode, spindle state, or feedrate,
 * Added support for Radius Mode G2/G3 motions,
 * Added customizable arc specification check in GCode preferences,
 * Reworked command panel for better button layout,
  
**Fixed**
 * Fixed G92 export,
 * Fixed G53 support,
 * Fixed a bug preventing from switching jog axis when using Shuttle Xpress devices,
 * Fixed spindle state display for Grbl,
 * Fixed broken spindle buttons in command panel,
 * Fixed Grbl not properly handling the end of a token execution,
 * Fixed TinyG not properly handling the end of a token execution,
 
