#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$CURRENT_DIR/common.sh"

MEDIA_SERVER_ID="$1"
if [[ -z ${MEDIA_SERVER_ID} ]]
then
  echo "You can specify video server id by calling \"$0 \"serverId1\""
  MEDIA_SERVER_ID="serverId1"
fi

sendFcmRequest "\"media_play\": {
          \"id\": \"${MEDIA_SERVER_ID}\",
          \"fromTitle\": \"terminal guigui\"
        }"
