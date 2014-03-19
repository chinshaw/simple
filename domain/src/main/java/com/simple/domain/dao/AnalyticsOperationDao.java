package com.simple.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.simple.domain.model.AnalyticsOperation;
import com.simple.domain.model.AnalyticsOperationName;
import com.simple.domain.model.Person;
import com.simple.domain.model.RAnalyticsOperation;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;
import com.simple.original.api.exceptions.DomainException;
import com.simple.original.api.exceptions.SimpleException;
import com.simple.original.api.orchestrator.IPerson;

/**
 * @author chinshaw
 */
public class AnalyticsOperationDao extends DaoBase<AnalyticsOperation> implements IDaoRequest<AnalyticsOperation> {

	/**
	 * Logger instance.
	 */
	private static final Logger logger = Logger.getLogger(AnalyticsOperationDao.class.getName());

	public AnalyticsOperationDao() {
		super(AnalyticsOperation.class);
	}

	/**
	 * This method requires that the current person either be set in the
	 * operation or that it will use the person from the current session.
	 */
	public Long save(AnalyticsOperation operation) throws DomainException {
		return this.saveAndReturn(operation).getId();
	}

	public AnalyticsOperation saveAndReturn(AnalyticsOperation operation) throws DomainException {
		Person currentPerson = operation.getOwner();
		if (currentPerson == null) {
			currentPerson = getCurrentPerson();

			if (currentPerson == null) {
				throw new DomainException("The person was not specified in entity and we were unable to retrieve the person from session");
			}
			operation.setOwner(currentPerson);
		}

		return super.saveAndReturn(operation);
	}

	public AnalyticsOperation copy(Long id) throws SimpleException {
		logger.fine("cloning operation id -> " + id);
		AnalyticsOperation original = null;
		AnalyticsOperation clone = null;
		original = find(id);
		clone = (AnalyticsOperation) original.clone();

		IPerson currentPerson = getEntityManager().merge(getSession().getCurrentPerson());
		if (currentPerson == null) {
			throw new RuntimeException("Trying to save new operation but it appears you are not authenticated");
		}
		setOwner(clone);
		return clone;
	}

	/**
	 * This will return just the list of operation names which contains the name
	 * and the id of the operation. This does return all the names for the
	 * operation.
	 * 
	 * @return
	 */
	public List<AnalyticsOperationName> listOperationNames() {
		EntityManager em = getEntityManager();

		TypedQuery<AnalyticsOperationName> query = em.createQuery(
				"select new com.simple.original.server.dao.AnalyticsOperationName(o.id, o.name) from AnalyticsOperation o order by o.name",
				AnalyticsOperationName.class);
		List<AnalyticsOperationName> operationNames = query.getResultList();

		return operationNames;
	}

	public List<AnalyticsOperation> search(int start, int max, RecordFecthType recordType, String searchText, String searchColumn,
			String sortColumn, SortOrder sortOrder) {

		EntityManager entityManager = getEntityManager();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<RAnalyticsOperation> criteriaQuery = criteriaBuilder.createQuery(RAnalyticsOperation.class);

		Root<RAnalyticsOperation> root = criteriaQuery.from(RAnalyticsOperation.class);

		List<Predicate> conditions = new ArrayList<Predicate>();

		if (searchColumn != null && (searchText != null && searchText.trim().length() > 0)) {
			conditions.add(criteriaBuilder.like(root.<String> get(searchColumn), "%" + searchText.trim() + "%"));
		}
		if (recordType != null) {
			boolean publicFlag = recordType.equals(RecordFecthType.PUBLIC_RECORDS);

			if (!recordType.equals(RecordFecthType.ALL_RECORDS)) {
				conditions.add(criteriaBuilder.equal(root.<Boolean> get("isPublic"), publicFlag));
			}
			if (recordType.equals(RecordFecthType.MY_RECORDS)) {
				IPerson currentPerson = getSession().getCurrentPerson();
				conditions.add(criteriaBuilder.equal(root.<Person> get("owner"), currentPerson));
			}
		}

		if (conditions.size() >= 1) {
			if (conditions.size() == 1) {
				criteriaQuery.where(conditions.get(0));
			} else {
				criteriaQuery.where(criteriaBuilder.and(conditions.toArray(new Predicate[0])));
			}
		}

		if (sortColumn != null && sortOrder != null) {

			if (sortColumn.equals("owner")) {
				List<Order> orders = new ArrayList<Order>();
				Path<Person> owner = root.<Person> get("owner");
				if (sortOrder == SortOrder.ASCENDING) {
					orders.add(criteriaBuilder.asc(root.<Boolean> get("isPublic")));
					orders.add(criteriaBuilder.asc(owner.<String> get("name")));
					criteriaQuery.orderBy(orders.toArray(new Order[0]));
				} else {
					orders.add(criteriaBuilder.desc(root.<Boolean> get("isPublic")));
					orders.add(criteriaBuilder.desc(owner.<String> get("name")));
					criteriaQuery.orderBy(orders.toArray(new Order[0]));
				}
			} else {
				if (sortOrder == SortOrder.ASCENDING) {
					criteriaQuery.orderBy(criteriaBuilder.asc(root.<String> get(sortColumn)));
				} else {
					criteriaQuery.orderBy(criteriaBuilder.desc(root.<String> get(sortColumn)));
				}
			}
		}
		Query query = entityManager.createQuery(criteriaQuery);
		query.setFirstResult(start);
		if (max > 0) {
			query.setMaxResults(max);
		}

		return query.getResultList();
	}

	public List<AnalyticsOperation> listAll() {

		TypedQuery<AnalyticsOperation> query = getEntityManager().createQuery("SELECT e FROM " + clazz.getName() + " e ORDER BY e.name",
				AnalyticsOperation.class);
		return query.getResultList();
	}

	public List<AnalyticsOperationInput> listInputs(Long operationId) {
		AnalyticsOperation operation = find(operationId);
		if (operation == null) {
			return null;
		}

		return operation.getInputs();
	}

	/**
	 * Sets the owner of the operation, this is typically necessary before
	 * saving it in the database due to the fact that an operation has a NotNull
	 * constraint on the owner.
	 * 
	 * @param operation
	 */
	private void setOwner(AnalyticsOperation operation) {
		Person currentPerson = getCurrentPerson();
		if (currentPerson == null) {
			throw new RuntimeException("Trying to save new operation but it appears you are not authenticated");
		}
		operation.setOwner(currentPerson);
	}
}