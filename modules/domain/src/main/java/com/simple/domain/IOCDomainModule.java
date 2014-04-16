package com.simple.domain;

import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.simple.domain.dao.AnalyticsOperationDao;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.domain.dao.DaoBase;
import com.simple.domain.dao.PersonDao;
import com.simple.domain.dao.SqlDataProviderDriverDao;
import com.simple.domain.dao.SqlDriverDao;

public class IOCDomainModule extends AbstractModule {

	@Override
	protected void configure() {
		//bindConstant().annotatedWith(Names.named("PERSITENCE_PU")).to(
		//		"simple-pu");
		install(new JpaPersistModule("simple-pu"));
		bind(PersistenceInitializer.class).asEagerSingleton();
		
		// Dao classes
		bind(DaoBase.class);
		bind(AnalyticsOperationDao.class);
		bind(AnalyticsTaskDao.class);
		bind(PersonDao.class);
		bind(SqlDataProviderDriverDao.class);
		bind(SqlDriverDao.class);
	}

	/*
	@Inject
	@Provides
	EntityManager getEntityManager(EntityManagerFactory factory) {
		return factory.createEntityManager();
	}

	@Inject
	@Provides
	@Singleton
	EntityManagerFactory getEntityManagerFactory(
			@Named("PERSITENCE_PU") String pu) {
		return Persistence.createEntityManagerFactory(pu);
	}
	*/
	
}
