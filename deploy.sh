#!/bin/bash

#$1 is baseFolder, $2 is subfolders array
deleteFolders(){
	for file in $(curl -s -l -u $VAR1:$VAR2 $TARGET$1); 
	do
	 echo "Removing file www/download/$1$file"
	 curl -u $VAR1:$VAR2 $TARGET$1 -X "DELE $file"
	done
	echo "Removing folder www/download/$1"
	curl -u $VAR1:$VAR2 $TARGET -X "RMD $1"
	
}

# Clean the distant repository
cleanRepository(){
  echo "Cleaning repository..."
  deleteFolders update/$gokoVersion/binary/
  deleteFolders update/$gokoVersion/plugins/
  deleteFolders update/$gokoVersion/features/
}

# Export the built repository to destination
exportRepository(){
  echo "Exporting repository..."
  # Switch to repository build location
  cd $TRAVIS_BUILD_DIR/org.goko.build.product/target/repository/
  
  find . -type f -exec curl --ftp-create-dirs -T {} -u $VAR1:$VAR2 $TARGET/$UPDATE_FOLDER/$gokoVersion/{} \;
}

# Export the built binaries to destination
exportRepository(){
  echo "Exporting binaries..."
  
  cd $TRAVIS_BUILD_DIR/org.goko.build.product/target/products/
  
  curl --ftp-create-dirs -T org.goko-linux.gtk.x86.zip -u $VAR1:$VAR2 $TARGET/$gokoVersion/oorg.goko-win32.win32.x86_64.zip
  curl --ftp-create-dirs -T org.goko-linux.gtk.x86.zip -u $VAR1:$VAR2 $TARGET/$gokoVersion/oorg.goko-win32.win32.x86.zip
  curl --ftp-create-dirs -T org.goko-linux.gtk.x86.zip -u $VAR1:$VAR2 $TARGET/$gokoVersion/org.goko-linux.gtk.x86_64.zip
  curl --ftp-create-dirs -T org.goko-linux.gtk.x86.zip -u $VAR1:$VAR2 $TARGET/$gokoVersion/org.goko-linux.gtk.x86.zip
  
}
# Let's do it
if [ $updateRepository == 'true' ]
then
  cleanRepository  
  exportRepository  
else
  echo "Skipped repository export..."
fi

if [ $updateBinaries == 'true' ]
then
	exportBinaries
else
  echo "Skipped binaries export..."
fi
