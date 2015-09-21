#!/bin/bash
echo "Exporting binaries..."

curl --ftp-create-dirs -T $HOME/org.goko.build.product/target/products/org.goko-win32.win32.x86_64.zip -u $VAR1:$VAR2 $TARGET/org.goko-win32.win32.x86_64.zip
