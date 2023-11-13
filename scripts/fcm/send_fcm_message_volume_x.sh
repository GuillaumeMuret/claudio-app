#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
source "$CURRENT_DIR/common.sh"

sendFcmRequest "\"$1\": \"notImportantValue\""
