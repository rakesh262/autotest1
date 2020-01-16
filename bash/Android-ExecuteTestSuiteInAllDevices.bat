@echo off

adb devices >ListOfConnectedDevices.tmp

echo Start in all devices...

for /F "tokens=*" %%A in (ListOfConnectedDevices.tmp) do (

timeout 1

if not "%%A" == "List of devices attached" (
    if not "%%A" == "* daemon started successfully *" (
	if not "%%A" == "adb server is out of date.  killing..." (
        echo Execute Device  %%A
	start Android-ExecuteTestSuite.bat
)
) 
)


