#!/usr/bin/bash
mkdir ~/.wesop
copy WeSOPServer-1.009-jar-with-dependencies.jar ~/.wesop/
copy ./run.sh ~/.wesop/
sudo copy wesop.desktop /etc/xdg/autostart
sudo apt-get update
sudo apt-get install ristretto
