#!/usr/bin/bash
cd /opt/wesop
java -jar --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.graphics,javafx.fxml "./WeSOPServer-1.007-jar-with-dependencies.jar"
