#!/bin/bash

APPS_DIR=/opt/artisan/apps
VERSIONS_DIR=${APPS_DIR}/versions

HADOOP_VERSION=hadoop-2.4.0
HBASE_VERSION=hbase-0.98.2
HBASE_HADOOP_VERSION=hadoop2
TOMCAT_VERSION=apache-tomcat-7.0.53
R_VERSION=R-3.1.0
ZOO_VERSION=zookeeper-3.4.6

HADOOP_VERSIONS_DIR=${VERSIONS_DIR}/${HADOOP_VERSION}
HBASE_VERSIONS_DIR=${VERSIONS_DIR}/${HBASE_VERSION}-${HBASE_HADOOP_VERSION}
TOMCAT_VERSIONS_DIR=${VERSIONS_DIR}/${TOMCAT_VERSION}
R_VERSIONS_DIR=${VERSIONS_DIR}/${R_VERSION}
ZOO_VERSIONS_DIR=${VERSIONS_DIR}/${ZOO_VERSION}

#HADOOP_URL=http://apache.mirrors.tds.net/hadoop/core/${HADOOP_VERSION}/${HADOOP_VERSION}.tar.gz
#HBASE_URL=http://apache.mirrors.tds.net/hbase/${HBASE_VERSION}/${HBASE_VERSION}-${HBASE_HADOOP_VERSION}-bin.tar.gz
# Note this url will not work in the future if the base version changes.
#TOMCAT_URL=http://www.dsgnwrld.com/am/tomcat/tomcat-7/v7.0.53/bin/${TOMCAT_VERSION}.tar.gz
#R_URL=http://cran.revolutionanalytics.com/src/base/R-3/${R_VERSION}.tar.gz
#ZOO_URL=http://www.trieuvan.com/apache/zookeeper/${ZOO_VERSION}/${ZOO_VERSION}.tar.gz

HADOOP_URL=file://$(pwd)/../resources/${HADOOP_VERSION}.tar.gz
HBASE_URL=file://$(pwd)/../resources/${HBASE_VERSION}-${HBASE_HADOOP_VERSION}-bin.tar.gz
TOMCAT_URL=file://$(pwd)/../resources/${TOMCAT_VERSION}.tar.gz
R_URL=file://$(pwd)/../resources/${R_VERSION}.tar.gz
ZOO_URL=file://$(pwd)/../resources/${ZOO_VERSION}.tar.gz


# r_arch=x86_64 F77="gfortran-4.2 -arch x86_64" --prefix=/opt/artisan/apps/versions/R-3.1.0 --without-x --with-cairo --with-libpng
export F77="gfortran-4.2 -arch x86_64"
R_CONFIG_OPTIONS="r_arch=x86_64 --enable-R-shlib --without-x --with-cairo --with-libpng"

do_install_hadoop() {
	if [[ ! -d ${HADOOP_VERSIONS_DIR} ]]; then
		cd ${VERSIONS_DIR} && curl ${HADOOP_URL} | tar xz
		cd ${APPS_DIR} && ln -sf  versions/${HADOOP_VERSION} hadoop
	fi;
}

do_install_hbase() {
	if [[ ! -d ${HBASE_VERSIONS_DIR} ]]; then
		cd ${VERSIONS_DIR} && curl ${HBASE_URL} | tar xz
		cd ${APPS_DIR} && ln -sf versions/${HBASE_VERSION}-${HBASE_HADOOP_VERSION} hbase
	fi;
}

do_install_tomcat() {
	if [[ ! -d ${TOMCAT_VERSIONS_DIR} ]]; then 
		cd ${VERSIONS_DIR} && curl ${TOMCAT_URL} | tar xz
		cd ${APPS_DIR} && ln -sf versions/${TOMCAT_VERSION} tomcat
	fi;
}

do_install_r() {
	if [[ ! -d ${R_VERSIONS_DIR} ]]; then
		r_install_base=$(mktemp -d /tmp/r_install.XXXXX)
		cd ${r_install_base} && curl ${R_URL} | tar xz
		cd ${r_install_base}/${R_VERSION}
		./configure --prefix=${R_VERSIONS_DIR} ${R_CONFIG_OPTIONS} && make && make install
		cd ${APPS_DIR} && ln -sf versions/${R_VERSION} R
	fi;
}

do_install_zoo() {
	if [[ ! -d ${ZOO_VERSIONS_DIR} ]]; then 
		cd ${VERSIONS_DIR} && curl ${ZOO_URL} | tar xz
		cd ${APPS_DIR} && ln -s versions/${ZOO_VERSION} zoo
	fi

}
	

install() {
	mkdir -p ${VERSIONS_DIR}
	do_install_zoo;
	do_install_hadoop;
	do_install_hbase;
	do_install_tomcat;
	do_install_r;
}

install;
