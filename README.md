# claudio-app

Media box application, whose main function is to launch sounds or videos directly onto friends' 
phones using FCM.

You can play media or TTS (Text To Speech) directly from application.
Check scripts in [this](#scripts-to-test-fcm-and-app) section.

# Demo

This is what we have for iOS / Android :

[![Claudio - iOS / Android - Compose Multiplatform](https://img.youtube.com/vi/lXzl4C-wwz0/maxresdefault.jpg)](https://youtu.be/lXzl4C-wwz0)

With Desktop and Android :

[![Claudio - Desktop / Android - Compose Multiplatform](https://img.youtube.com/vi/HWrvUPPXJwE/maxresdefault.jpg)](https://youtu.be/HWrvUPPXJwE)

With Browser and Android :

[![Claudio - Browser / Android - Compose Multiplatform](https://img.youtube.com/vi/I5_k4S3OWzM/maxresdefault.jpg)](https://youtu.be/I5_k4S3OWzM)


# Build

This section will present the tools and other setup you must do to build the apps correctly :

## Tools

You must have installed on your machine :
- XCode from AppStore and enable the command line tools
- The first time you may also need to accept its license terms and allow it to perform some 
necessary initial tasks.
- CocoaPods `brew install cocoapods`
- Android SDK
- MQTT - <ins>Not working anymore</ins> since new kotlin version 😞 (Check the "TODO MQTT LIB 
ISSUE") init MQTT libraries : [scripts/libs/init-maven.sh](scripts/libs/init-maven.sh)

## Environment variables

You will have to declare the environment variables used in all application even in the scripts.
You must declare :


The base url of the claudio backend service :

`CLAUDIO_BASE_URL=https://claudio.io`

The auth token used to get the media :

`CLAUDIO_AUTH_TOKEN=<<CLAUDIO_AUTH_TOKEN>>`

The Slack end url used for the hook. 

The end of this begin url -> https://hooks.slack.com/services/

`SLACK_END_URL=<<SLACK_END_URL>>`

The FCM server key used to send messages :

`FCM_SERVER_KEY=<<FCM_SERVER_KEY>>`

## Special configuration

This section explain the special configuration you must do to build and run app correctly :

### Android

You have to add the `google-services.json` in the `appAndroid` module to use the FCM service 
correctly. 

Ask a developer to get the file 😊

### JavaScript

There is an issue with the resources embedded on the app.

To add the resources, use this workaround gradle task : `./gradlew :appJs:copyJsResourcesWorkaround`

# Tools

Some tools has been added on this project :

## Check dependencies to update

`./gradlew dependencyUpdates -Drevision=release`

## Scripts to test FCM and app

This section will present the script you can use to test app with FCM messages and the API. 
Make sure you have declared a FCM token of a terminal like so :

`TEST_TERMINAL_KEY=<<TEST_TERMINAL_KEY>>`

Be careful of the spaces around the `=`. 

Make sure there is no space before and after to avoid 
issues with the scripts.

### Test app media play

You can use the test script 
[scripts/fcm/send_fcm_message_video_play.sh](scripts/fcm/send_fcm_message_media_play.sh). 

This 
script send a request to play media on device.

### Test app kill player

You can use the test script 
[scripts/fcm/send_fcm_message_kill_player.sh](scripts/fcm/send_fcm_message_kill_player.sh). 

This script send a request to kill all players running on device.

### Test app volume lower

You can use the test script 
[scripts/fcm/send_fcm_message_volume_lower.sh](scripts/fcm/send_fcm_message_volume_lower.sh). 

This script send a request to lower volume on device.

### Test app volume max

You can use the test script 
[scripts/fcm/send_fcm_message_volume_max.sh](scripts/fcm/send_fcm_message_volume_max.sh). 

This script send a request to set volume at maximum value on device.

### Test app volume min

You can use the test script 
[scripts/fcm/send_fcm_message_volume_min.sh](scripts/fcm/send_fcm_message_volume_min.sh). 

This script send a request to set volume at minimum value on device.

### Test app volume raise

You can use the test script 
[scripts/fcm/send_fcm_message_volume_raise.sh](scripts/fcm/send_fcm_message_volume_raise.sh). 

This script send a request to raise volume on device.

### Test app tts

You can use the test script
[scripts/fcm/send_fcm_message_tts.sh](scripts/fcm/send_fcm_message_tts.sh).

From the script you can change request body to play with message / language / pitch / speed.

# TODO List

- Use [SQLDelight](https://github.com/cashapp/sqldelight) for database
- Use FCM service on iOS
- Use a common video player (check this 
[article](https://medium.com/proandroiddev/unifying-video-players-compose-multiplatform-for-ios-android-desktop-aa920d29bbf3)
)
- Use stringResource for the strings
- Use [Gradle Version Catalogs](https://developer.android.com/build/migrate-to-catalogs)
- Add postman collection
- Add a script to add new media
- Add application function to add a new media
- Make swagger
- Use [kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime) for the date dislay
