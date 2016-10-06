# Goko [![Build Status](https://travis-ci.org/cncgoko/Goko.svg?branch=master)](https://travis-ci.org/cncgoko/Goko) [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/cncgoko/Goko?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=body_badge)

Current version  : ```0.3.1```

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

### Added
 * GCode text editor with syntax highlighting and error detection,,
 * Added keybinding for Shuttle Xpress for _Homing Sequence_, and _Reset Zero_,
 * Finer control over grid opacity,
 * Customizable background color in 3D preview,
 * Customizable camera controls (https://github.com/cncgoko/Goko/issues/29[Issue #29])
 * More documentation http://docs.goko.fr/
 * Added array modifier

### Fixed
 * Fixed Translate modifier to apply translation to motion only if they are affected on one axis at least,
 * Fixed execution monitor sometimes missing the few last commands of a file.
 * Fixed Restore Defaults for Quantity fields in preferences. It no longer writes unit in quantity field.

[Full changelog](http://docs.goko.fr/master/documentation/changelog.html)
