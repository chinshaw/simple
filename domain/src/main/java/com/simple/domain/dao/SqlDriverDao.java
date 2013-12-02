package com.simple.domain.dao;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.google.inject.name.Named;
import com.simple.domain.SqlDriver;

public class SqlDriverDao {
    
    private static final Logger logger = Logger.getLogger(SqlDriverDao.class.getName());
    
    
    @Named("com.simple.original.server.domain.sqldataprovider.xml.path")
    private static String driversPath;
    
    @XmlRootElement
    static class SqlDrivers {
        /**
         * This is a list of cell phone provider  that we are subscribed to.
         */
        @XmlElement(name = "sqlDriver")
        private List<SqlDriver> sqlDrivers;

        public List<SqlDriver> getDrivers() {
            return sqlDrivers;
        }
    }
    
    public static List<SqlDriver> getSqlDrivers() throws JAXBException   {
        SqlDrivers sqlDrivers = null;
        
        JAXBContext context = JAXBContext.newInstance(SqlDrivers.class, SqlDriver.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        
        logger.info("Driver path for sql drivers -> " + driversPath);
        
        InputStream inputStream = SqlDriverDao.class.getResourceAsStream(driversPath);
        logger.info("Input stream -> " + inputStream);
        
        try {
            sqlDrivers = (SqlDrivers) unmarshaller.unmarshal(inputStream);
            logger.info("Number if drivers found is " + sqlDrivers.getDrivers().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return sqlDrivers.getDrivers();
    }
    
    public static SqlDriver getDriver(String name) throws XPathExpressionException, JAXBException {
        SqlDriver sqlDriver = null;
        
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        
        InputStream inputStream = SqlDriverDao.class.getResourceAsStream(driversPath);
        InputSource source = new InputSource(inputStream);
        XPathExpression expr = xpath.compile("/sqlDrivers/sqlDriver[driverName='" + name + "']");
        
        Node node = (Node) expr.evaluate(source, XPathConstants.NODE);
        
        System.out.println("Node is " + node.getTextContent());
        
        JAXBContext context = JAXBContext.newInstance(SqlDriver.class);

        Unmarshaller unmarshaller = context.createUnmarshaller();
        sqlDriver = (SqlDriver) unmarshaller.unmarshal(node);
        
        return sqlDriver;
    }
}
