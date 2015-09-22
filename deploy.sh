#!/bin/bash
cd /home/travis/build/cncgoko/Goko/org.goko.build.product/target/repository/

if [ $updateRepository == 'true' ]
then
  cleanRepository  
  exportRepository  
else
  echo "Skipped repository export..."
fi

cleanRepository(){
  echo "Cleaning repository..."
  find . -type f -exec curl -u $VAR1:$VAR2 $TARGET/$gokoVersion/ -Q "DELE {}" \;
}

exportRepository(){
  echo "Exporting repository..."
  # TODO change target to remove hardcoded 'nightly'
  find . -type f -exec curl --ftp-create-dirs -T {} -u $VAR1:$VAR2 $TARGET/$gokoVersion/{} \;
}
