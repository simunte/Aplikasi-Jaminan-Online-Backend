#!/bin/sh

WORKING_DIR="/app"
ATF="ajo.api"
VERSION="0.0.1-SNAPSHOT"
APP_NAME="$WORKING_DIR/$ATF-$VERSION.jar"
ROOT_APP="$WORKING_DIR"
DATE=`date '+%Y-%m-%d-%H%M%S'`

# JVM Configuration
JVM_ARGS="-server -XX:MaxNewSize=64m -XX:NewSize=64m -Xms512m -Xmx1024m"
JVM_ARGSA="$JVM_ARGS -Dspring.profiles.active=azure"
JVM_ARGSP="$JVM_ARGS -Dspring.profiles.active=production"

if [ "$1" ]; then

        if [ "$1" = "start" ]; then

            if [ "$2" = "azure" ]; then
                echo "starting azure $APP_NAME..."
                /usr/bin/java -DROOT_APP=$ROOT_APP $JVM_ARGSA -jar $APP_NAME > $WORKING_DIR/logs/backend-$DATE.log &

            elif [ "$2" = "production" ]; then
                echo "starting production $APP_NAME..."
                /usr/bin/java -DROOT_APP=$ROOT_APP $JVM_ARGSP -jar $APP_NAME > $WORKING_DIR/logs/backend-$DATE.log &
        else
            echo "Please give argument 'azure' or 'production' ..."

        fi

        elif [ "$1" = "stop" ]; then
                echo "stopping $APP_NAME..."
                pkill -f $APP_NAME
                echo "$APP_NAME successfully stopped."
        fi
else
    echo "Please give first argument 'start' or 'stop' and the second argument is 'azure' or 'production'"
fi