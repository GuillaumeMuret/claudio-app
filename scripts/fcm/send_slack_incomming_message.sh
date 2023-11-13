#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$CURRENT_DIR/common.sh"

curl -X POST "https://hooks.slack.com/services/${SLACK_END_URL}" -H "accept: application/json" -d '{"text":"Salut"}'
