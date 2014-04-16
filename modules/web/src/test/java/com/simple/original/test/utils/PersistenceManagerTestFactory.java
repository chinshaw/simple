package com.simple.original.test.utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * This is the testing persistence manager, it should mirror the main
 * PersistnceManager but will use the virtualfactory-test database, which 
 * can be destroyed or created as necessary
 * 
 * 
 * See the META-INF/persistence.xml and META-INF/orm.xml for more details
 * 
 * @author chinshaw
 *
 */
public class PersistenceManagerTestFactory {

    private static final EntityManagerFactory PMF = Persistence.createEntityManagerFactory("openjpa-test-pu");

    public static EntityManagerFactory getEntityManagerFactory() {
        return PMF;
    }
}
