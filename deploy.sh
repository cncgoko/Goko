#!/bin/bash

renameFolders(){
	echo "Renaming folder $1 to $2"
	curl -u $FTP_USER:$FTP_PASS ftp://ftp.goko.fr/www/docs/ -Q "-RNFR $1" -Q "-RNTO $2"

}

# Archive the distant repository
archiveRepository(){
  echo "Archiving repository..."
  timestamp=`date +"%Y%m%d%H%M%S"`
  archivedRepoName=$gokoVersion"_"$timestamp
  curl -u $VAR1:$VAR2 $TARGET$1/update/ -Q "-RNFR $gokoVersion" -Q "-RNTO $archivedRepoName"
  
}

# Export the built repository to destination
exportRepository(){
  echo "Exporting repository..."
  # Switch to repository build location
  cd $TRAVIS_BUILD_DIR/org.goko.build.product/target/repository/
  
  find . -type f -exec curl --ftp-create-dirs -T {} -u $VAR1:$VAR2 $TARGET/$UPDATE_FOLDER/$gokoVersion/{} \;
}

# Let's do it
if [ $updateRepository == 'true' ]
then
	if curl --output /dev/null --silent --head --fail "$url"; then
		# previous repo already exists, let's archive it
		archiveRepository
	fi
  
  exportRepository  
else
  echo "Skipped repository export..."
fi
