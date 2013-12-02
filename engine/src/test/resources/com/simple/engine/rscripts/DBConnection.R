library(RJDBC); # To work in Java 
drv <- JDBC('com.microsoft.sqlserver.jdbc.SQLServerDriver', '/usr/local/virtualfactory/dataproviders/jdbc/sqljdbc4.jar', identifier.quote="'"); 
ch <- dbConnect(drv, 'jdbc:sqlserver://hvfabs104.ccm.cpqcorp.net;databaseName=VirtualFactoryDW', 'VFReader', 'easy24get');