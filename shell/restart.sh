#!/usr/bin/env bash
source ~/.bash_profile

#cd /home/user1/demo/
#mv -f boot-admin.jar  boot-admin.jar.bak
#cp -rf ./tmp/*.jar ./

ID=`ps -ef | grep boot-admin | grep -v "grep" | awk '{print $2}'`
echo $ID
for id in $ID
do
kill -9 $id
done

nohup java ${JAVA_OPTS} -jar boot-admin.jar ${JAVA_WEB_SERVICE_OPTS} &> /dev/null &
