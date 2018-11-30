@echo off
setlocal
set ngoyVersion=beta6
set ngoyPath=build\tmp\ngoy-%ngoyVersion%

if not exist %ngoyPath% (
	echo Extracting ngoy binaries...
	call gradle extractNgoy
)
java -cp %ngoyPath%\* ngoy.Ngoy %*