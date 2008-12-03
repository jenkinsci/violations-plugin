@echo off
echo Removing build history...
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\builds
for /d %%i in (work\jobs\*) do del /s /q %%i\nextBuildNumber
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\modules
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\workspace\target
for /d %%i in (work\jobs\*) do for /d %%j in (%%i\workspace\*) do rmdir /s /q %%j\target
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\workspace\dist
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\workspace\build
rmdir /s /q work\fingerprints
del work\queue.txt
del work\secret.key
del work\update-center.json
