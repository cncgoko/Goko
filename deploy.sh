#!/bin/bash

# Clean the distant repository
cleanRepository(){
  echo "Cleaning repository..."
#  curl -u $VAR1:$VAR2 $TARGET/ -Q "RMD 0.0.2/"
  curl -u $VAR1:$VAR2 $TARGET/ -Q "mkdir 0.0.3"
  curl -u $VAR1:$VAR2 $TARGET/ -Q "mkdir 0.0.4"
  curl -u $VAR1:$VAR2 $TARGET/ -Q "rmdir 0.0.3"
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
