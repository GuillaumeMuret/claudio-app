#!/bin/bash

CURRENT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )
$CURRENT_DIR/send_fcm_message_volume_x.sh "volume_max"
