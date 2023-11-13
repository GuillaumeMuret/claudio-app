#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$CURRENT_DIR/common.sh"

STR_TO_SEND=$1
if [[ -z ${STR_TO_SEND} ]]
then
  echo "You can add a message by calling \"$0 \"Un super texte à envoyer ;)\""
  STR_TO_SEND="Bonjour Niji !"
fi

sendFcmRequest "\"tts\": {
          \"message\": \"${STR_TO_SEND}\",
          \"language\": \"fr\",
          \"pitch\": 1.0,
          \"speed\": 1.5
        }"
