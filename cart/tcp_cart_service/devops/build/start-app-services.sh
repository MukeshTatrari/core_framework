#!/bin/sh
set -e
echo "Printing all ENV variables for this shell scope."
echo "================================================================================================================================="
printenv | grep -v SPRING_CLOUD_CONFIG_SERVER_GIT_USERNAME | grep -v SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD
echo "================================================================================================================================="
echo "Starting spring boot app...."
echo "================================================================================================================================="
echo "Using JAVA_OPTS = $JAVA_OPTS"
echo "================================================================================================================================="
if [ -z $APPDYNAMICS_AGENT_APPLICATION_NAME ]; 
then
  java $JAVA_OPTS -jar /opt/tcp/app/*.jar
else 
	java $JAVA_OPTS -Dappdynamics.jvm.shutdown.mark.node.as.historical=true -Djava.security.egd=file:/dev/./urandom -javaagent:/opt/tcp/appdynamics/javaagent.jar=uniqueID=$(hostname) -jar /opt/tcp/app/*.jar
fi
################################################