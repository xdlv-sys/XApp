sc stop Tomcat8
Wscript sleep.vbs

cd ../../../../../

rem file: del /Q file 
rem dir : rmdir /Q /S dir

xcopy webapps\ROOT\WEB-INF\patch\tmp\version .\ /E /Q /Y
sc start Tomcat8