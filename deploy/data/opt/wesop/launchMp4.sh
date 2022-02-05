#!/bin/bash

while :
do
	killall mplayer
	echo 'loop'
        #nohup mpv --fullscreen --stop-screensaver $1 >> /opt/wesop/stdout.log &
        nohup mplayer -vf scale -fs $1 > /opt/wesop/stdout.log 2>/opt/wesop/stderr.log &
        #nohup cvlc --fullscreen $1 > /opt/wesop/stdout.log 2>/opt/wesop/stderr.log &
        process_id=$!
        wait $process_id
        killall mplayer
        sleep 1
done
