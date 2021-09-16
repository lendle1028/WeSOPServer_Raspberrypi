#!/bin/bash

while :
do
	killall mplayer
	echo 'loop'
        #nohup mpv --fullscreen --stop-screensaver $1 >> /opt/wesop/stdout.log &
        nohup mplayer -fs $1 >> /opt/wesop/stdout.log &
        process_id=$!
        wait $process_id
        killall mplayer
        sleep 1
done
