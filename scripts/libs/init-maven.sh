#!/bin/bash

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

KMQTT_VERSION=0.4.1

cd $SCRIPT_DIR/../..
mkdir -p libs/mqtt
cd libs/mqtt
if [[ -d KMQTT ]]; then
  echo "Dir exist"
else
  git clone https://github.com/davidepianca98/KMQTT.git --depth 1 --branch ${KMQTT_VERSION}
fi
cd KMQTT
chmod u+x gradlew
./gradlew publishToMavenLocal

# cd ${SCRIPT_DIR}/../..
# cp ${HOME}/.m2/repository/com/github/davidepianca98/kmqtt-common-iosarm64/${KMQTT_VERSION}/kmqtt-common-iosarm64-0.4.1-sources.jar ./libs/mqtt
# cp ${HOME}/.m2/repository/com/github/davidepianca98/kmqtt-client-iosarm64/${KMQTT_VERSION}/kmqtt-client-iosarm64-0.4.1-sources.jar ./libs/mqtt
# cp ${HOME}/.m2/repository/com/github/davidepianca98/kmqtt-common-iosx64/${KMQTT_VERSION}/kmqtt-common-iosx64-0.4.1-sources.jar ./libs/mqtt
# cp ${HOME}/.m2/repository/com/github/davidepianca98/kmqtt-client-iosx64/${KMQTT_VERSION}/kmqtt-client-iosx64-0.4.1-sources.jar ./libs/mqtt
