#!/bin/bash

SOURCE_DIR="src"
BIN_DIR="bin"
MAIN_CLASS="bankapp.Menu"

mkdir -p "$BIN_DIR"
javac -d "$BIN_DIR" "$SOURCE_DIR"/bankapp/*.java

if [ $? -ne 0 ]; then
	echo " >>> Complication FAIL! <<< "
	exit 1
else
	echo "Compliation successful."
fi

java -cp "$BIN_DIR" "$MAIN_CLASS"
exit 0
