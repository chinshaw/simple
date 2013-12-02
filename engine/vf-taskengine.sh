#!/bin/sh -x


LOGGING_PROPS="-Dlog4j.configuration=file:log4j.xml"


java ${LOGGING_PROPS} -jar -classpath lib/ -Dcom.hp.taskengine.properties=taskengine-server.properties -Djava.security.policy=src/META-INF/taskenginesecurity.policy deploy/taskengine-server.jar
#java ${LOGGING_PROPS} -jar -classpath lib/ -Djava.security.policy=src/META-INF/taskenginesecurity.policy deploy/taskengine-server.jar
