@echo off
cd "%1"
del *-min.js
for /f %%a in ('dir /A:-D /S /b *.js') do call:ProcessCompress: %%a
rem for /f %%a in ('dir /A:-D /S /b *.css') do call:ProcessCompress: %%a

:ProcessCompress
IF NOT [%1]==[] call:CompressFiles: %1
GOTO:EOF

:CompressFiles
echo %1 "->" %~n1-min%~x1
java -jar yuicompressor-2.4.8.jar %1 --charset utf-8 -o %~n1-min%~x1
GOTO:EOF