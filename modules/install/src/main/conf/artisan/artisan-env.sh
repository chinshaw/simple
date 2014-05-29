#!/bin/bash -x


export ARTISAN_HOME="/opt/artisan"
export ARTISAN_CONF_DIR="${ARTISAN_HOME}/conf"
export APPS_DIR="${ARTISAN_HOME}/apps"

export HADOOP_PREFIX="${APPS_DIR}/hadoop"
export HADOOP_CONF_DIR="${ARTISAN_CONF_DIR}/hadoop"
export HADOOP_BIN="${APPS_DIR}/hadoop/sbin"

export HBASE_CONF_DIR="${ARTISAN_CONF_DIR}/hbase"
export HBASE_HOME="${APPS_DIR}/hbase"
export HBASE_BIN="${APPS_DIR}/hbase/bin"

export ZOOBINDIR="/opt/artisan/data/zookeeper/bin"
export ZOOCFGDIR="${ARTISAN_CONF_DIR}/zoo"
export ZOOKEEPER_BIN="${APPS_DIR}/zoo/bin"

export TOMCAT_BIN="${APPS_DIR}/tomcat/bin"

# Figure out what kind of platform we are running on
platform='unknown'
unamestr=`uname`
if [[ "$unamestr" == 'Linux' ]]; then
   platform='linux'
elif [[ "$unamestr" == 'FreeBSD' ]]; then
   platform='freebsd'
elif [[ "$unamestr" == 'Darwin' ]]; then
   platform='osx'
fi


# This will set the JAVA_HOME but we only care about osx and linux
if [[ $platform == 'osx' ]]; then
	export JAVA_HOME=$(/usr/libexec/java_home)
elif [[ $platform == 'linux' ]]; then
	export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
fi


