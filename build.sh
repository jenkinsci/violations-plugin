#!/bin/bash

function build_clean {
 for KILLPID in `ps ax | grep 'hpi:run' | awk ' { print $1;}'`; do
  echo "Jenkins is running at $KILLPID, killing it"
  kill $KILLPID || echo;
 done
}

##
## Setup
##
echo Setting up Maven
mkdir -p ~/.m2
[ -f ~/.m2/settings.xml.backup ] || cp ~/.m2/settings.xml ~/.m2/settings.xml.backup
cp sandbox/settings.xml ~/.m2/settings.xml

##
## Build plugin
##
echo Building plugin
mvn -q package || exit 1

##
## Start Jenkins on localhost
##
echo Starting Jenkins on localhost
build_clean
JENKINS_PORT=8123
JENKINS_PREFIX=/jenkins
mvn -q hpi:run -Djetty.port=$JENKINS_PORT -Dhpi.prefix=$JENKINS_PREFIX || exit 1 &
JENKINS_URL=http://localhost:$JENKINS_PORT$JENKINS_PREFIX
until $(curl --output /dev/null --silent --head --fail $JENKINS_URL); do
    printf '.'
    sleep 5
done
echo Jenkins started at $JENKINS_URL

##
## Test plugin
##
cd  plugin-test
mvn -q test -Djenkins=$JENKINS_URL -Dheadless=true || exit 1
cd ..

##
## Exit
##
build_clean
