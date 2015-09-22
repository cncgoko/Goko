#!/bin/bash

#$1 is baseFolder, $2 is subfolders array
deleteFolders(){
	for folder in $2
	do
		for file in $(curl -s -l -u $VAR1:$VAR2 ftp://ftp.goko.fr/www/download/$1/$folder); 
		do
		 curl -u $VAR1:$VAR2 ftp://ftp.goko.fr/www/download/$1/$folder -Q "RM $file";
		done
		curl -u $VAR1:$VAR2 ftp://ftp.goko.fr/www/download/$1/ -Q "RMD $folder";
	done
}

# Clean the distant repository
cleanRepository(){
  echo "Cleaning repository..."
  deleteFolders update/nightly/ ('binary')
  #curl -u $VAR1:$VAR2 $TARGET/ -X "rm -r 0.0.2"
  #curl -u $VAR1:$VAR2 $TARGET/ -X "RMD 0.0.2"
}

# Clean the distant repository
exportRepository(){
  echo "Exporting repository..."
  # Switch to repository build location
  cd $TRAVIS_BUILD_DIR/org.goko.build.product/target/repository/
  # TODO change target to remove hardcoded 'nightly'
  #   find . -type f -exec curl --ftp-create-dirs -T {} -u $VAR1:$VAR2 $TARGET/$gokoVersion/{} \;
}

# Let's do it
if [ $updateRepository == 'true' ]
then
  cleanRepository  
  exportRepository  
else
  echo "Skipped repository export..."
fi
