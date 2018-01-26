@echo off
SET SBT_OPTS=-Xms1024M -Xmx2048M -Xss1M -XX:+CMSClassUnloadingEnabled -Dfile.encoding=utf8 -Djava.compiler=NONE
REM java %SBT_OPTS% -Xnoagent -jar %~dp0/sbt-launch.jar %1 %2
java %SBT_OPTS% -Xnoagent -Djava.compiler=NONE -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=4000 -jar %~dp0/sbt-launch.jar %1 %2