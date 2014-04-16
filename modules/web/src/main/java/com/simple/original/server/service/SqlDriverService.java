package com.simple.original.server.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPathExpressionException;

import com.simple.domain.dao.SqlDriverDao;
import com.simple.domain.model.SqlConnection;
import com.simple.domain.model.SqlDriver;
import com.simple.original.shared.SqlDataProviderException;

public class SqlDriverService {

    /**
     * This is used to test the connection of a sql data provider driver, it
     * will return a boolean if it suceeded and can also throw exceptions if
     * there is a problem.
     * 
     * @param driver
     * @return boolean
     * @throws SqlDataProviderException 
     * @throws JAXBException 
     * @throws XPathExpressionException 
     */
    public static boolean testConnection(SqlConnection driver) throws SqlDataProviderException, XPathExpressionException, JAXBException {
        boolean isConnected = false;

        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            throw new SqlDataProviderException("Could not load drivers");
        }
        
        try {
            String connectString = getJDBCConnectString(driver);
            Connection conn = DriverManager.getConnection(connectString, driver.getUserName(), driver.getPassword());
            isConnected = conn.isValid(5);
            conn.close();
        } catch (SQLException e) {
            throw new SqlDataProviderException("Connection to driver " + driver.getDriverName() + " failed ", e);
        }

        return isConnected;
    }

    /**
     * This will create a jdbc connect string from the SqlDataProviderDriver and it's correlating
     * SqlDriver. 
     * @param provider
     * @return
     * @throws JAXBException 
     * @throws XPathExpressionException 
     * @throws SqlDataProviderException 
     */
    public static String getJDBCConnectString(SqlConnection provider) throws XPathExpressionException, JAXBException, SqlDataProviderException {
        SqlDriver driver = SqlDriverDao.getDriver(provider.getDriverName());
        
        if (driver == null) {
            throw new SqlDataProviderException("Unable to find SqlDriver by name of " + provider.getDriverName());
        }
        System.out.println("Driver is " + driver.getDriverName() + " type " + driver.getDriverType());
        
        String connectionString = "jdbc:" + driver.getDriverType() + "://" + provider.getHost();
        
        return connectionString;
    }
}
