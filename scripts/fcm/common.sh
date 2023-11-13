#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

function getEnvVar() {
  ENV_VAR=$1
  ENV_VAR_STR=$(cat ${CURRENT_DIR}/../../local.properties | grep ${ENV_VAR})
  echo ${ENV_VAR_STR#*=}
}

export SLACK_END_URL=$(getEnvVar SLACK_END_URL)
TEST_TERMINAL_KEY=$(getEnvVar TEST_TERMINAL_KEY)
FCM_SERVER_KEY=$(getEnvVar FCM_SERVER_KEY)

function sendFcmRequest {
  DATA=$1
  echo $DATA
  curl -X POST --header "Authorization: key=${FCM_SERVER_KEY}" \
    --header "Content-Type: application/json" \
    https://fcm.googleapis.com/fcm/send \
    -d "
    {
      \"to\":\"${TEST_TERMINAL_KEY}\",
      \"data\": {
        $DATA
      }
    }"
}

export -f sendFcmRequest
