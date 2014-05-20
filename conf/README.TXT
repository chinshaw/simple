Configurations

Overview

This directory contains the configuration files for all external applications. Configuration
directories are organized by the name of the application. These files should be copied to the
artisan configuration directory after deployment. All daemons should be updated to use their
configuration directory rather than the installation configuration directory.



Hadoop

Hadoop configuration is pretty simple you only need to edit one line of the default installation
to configure it to use the artisan configuration. In the installation directory edit the 
etc/hadoop/hadoop-env.sh file and configure the HADOOP_CONF_DIR to point to the deployed configuration
directory.
