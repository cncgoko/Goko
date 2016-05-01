# Goko [![Build Status](https://travis-ci.org/cncgoko/Goko.svg?branch=master)](https://travis-ci.org/cncgoko/Goko) [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cncgoko/Goko?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=body_badge)

Current version  : ```0.3.0```

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

You can also discuss on [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cncgoko/Goko?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=body_badge)

##Changelog

###Added
- Support for **Grbl v0.9**
- New GCode parsing functionalities,
- Added GCode modifiers support (Translate, scale, segmentize...),
- Added **auto-leveler**,
- Added **wrapper** to convert GCode to 4 axis,
- Added support for **Shuttle XPress** device,
- Added save/load project features,
- Added execution queue for multiple files execution,
- Improved jog functionnality,
- Online documentation http://docs.goko.fr/

###Fixed
- Fixed Grbl buffering issue, 
- Fixed High CPU while connected to serial - [Issue #22](https://github.com/cncgoko/Goko/issues/22)
