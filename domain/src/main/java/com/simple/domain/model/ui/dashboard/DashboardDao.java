 package com.simple.domain.model.ui.dashboard;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.simple.domain.dao.AnalyticsTaskDao;
import com.simple.domain.dao.DaoBase;
import com.simple.domain.dao.IDaoRequest;
import com.simple.domain.model.AnalyticsTask;
import com.simple.original.api.exceptions.DomainException;

public class DashboardDao extends DaoBase<Dashboard> implements IDaoRequest<Dashboard> {

	
	@Inject
	AnalyticsTaskDao taskDao;
	
	public DashboardDao() {
		super(Dashboard.class);
	}
	
    public Dashboard getDashboardForDesigner(Long dashboardId) {

        EntityManager em = getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Dashboard> criteriaQuery = cb.createQuery(Dashboard.class);

        Root<Dashboard> root = criteriaQuery.from(Dashboard.class);
        
        root.fetch("widgets", JoinType.LEFT);

        criteriaQuery.where(cb.equal(root.get("id"), dashboardId));

        TypedQuery<Dashboard> query = em.createQuery(criteriaQuery);
        return query.getSingleResult();   
    }

    @Override
    public Long save(Dashboard dashboard) throws DomainException {
        return super.save(dashboard);
    }

    public Dashboard findDashboardForTask(AnalyticsTask task) {
        return findDashboardForTask(task.getId());
    }

    public Dashboard findDashboardForTask(Long taskId) {
        Dashboard dashboard = null;

        EntityManager em = getEntityManager();

        try {
            TypedQuery<Dashboard> query = em.createNamedQuery("findDashboardByTaskId", Dashboard.class);
            query.setParameter("taskId", taskId);

            dashboard = query.getSingleResult();
        } catch (NoResultException e) {
            // Didn't find a result but we want to return null instead of
            // throwing runtime
            // exception
            dashboard = null;
        }
        return dashboard;
    }
}
