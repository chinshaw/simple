package com.simple.domain.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.inject.Inject;
import com.simple.api.domain.RecordFecthType;
import com.simple.api.domain.SortOrder;
import com.simple.api.exceptions.DomainException;
import com.simple.api.exceptions.SimpleException;
import com.simple.api.exceptions.TooManyResultsException;
import com.simple.api.orchestrator.IDatastoreObject;
import com.simple.api.orchestrator.IPerson;
import com.simple.domain.model.Person;
import com.simple.security.api.ISession;

public class DaoBase<T extends IDatastoreObject> {

	private final Logger logger = Logger.getLogger(DaoBase.class.getName());

	protected Class<T> clazz;

	private EntityManager em;

	private ISession session;
	
	public DaoBase(){}

	public DaoBase(Class<T> clazz) {
		this.clazz = clazz;
		// Unfortunately this doesn't work with guice-persist
		//clazz = (Class<T>) ((ParameterizedType) getClass()
		//		.getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	public T create(T obj) throws DomainException {
		return saveAndReturn(obj);
	}
	

	/**
	 * This uses the merge function and not the persist function. That way it
	 * can be used to update and create a new entity. There are some small
	 * caveats with this but nothing major.
	 * http://stackoverflow.com/questions/1069992
	 * /jpa-entitymanager-why-use-persist-over-merge
	 * 
	 * @param Object
	 *            to be persisted.
	 * @return The persisted object
	 */
	//@Transactional
	protected T saveOrUpdate(T obj) {
		em.getTransaction().begin();
		obj = em.merge(obj);
		em.getTransaction().commit();
		
		return obj;
	}

	/**
	 * This will save the object and return the id of the persisted object.
	 * 
	 * @param obj
	 * @return
	 */
	public Long save(T obj) throws DomainException {
		return saveOrUpdate(obj).getId();
	}

	public T saveAndReturn(T obj) throws DomainException {
		return saveOrUpdate(obj);
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException
	 */
	public T find(Long id) {
		if (id == null) {
			return null;
		}

		T obj = em.find(clazz, id);
		return obj;
	}

	public List<T> findList(Set<Long> ids) {
		TypedQuery<T> q = em.createQuery(
				"select entity from " + clazz.getName()
						+ " entity where entity.id IN :ids", clazz);
		q.setParameter("ids", ids);

		return (List<T>) q.getResultList();
	}

	/**
	 * @param start
	 *            Starting record
	 * @param max
	 *            Max number of records to fetch and return
	 * @param recordType
	 *            RecordFecthType
	 * @param searchText
	 *            Search text
	 * @param searchColumn
	 *            Search column
	 * @param sortColumn
	 *            Sort column
	 * @param sortOrder
	 *            SortOrder
	 * @return List of entities
	 */
	public List<T> search(int start, int max, RecordFecthType recordType,
			String searchText, String searchColumn, String sortColumn,
			SortOrder sortOrder) {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);

		Root<T> root = criteriaQuery.from(clazz);

		List<Predicate> conditions = new ArrayList<Predicate>();

		if (searchColumn != null
				&& (searchText != null && searchText.trim().length() > 0)) {
			searchText = searchText.trim().toLowerCase();
			conditions.add(criteriaBuilder.like(
					criteriaBuilder.lower(root.<String> get(searchColumn)), "%"
							+ searchText + "%"));
		}

		if (recordType != null) {
			boolean publicFlag = recordType
					.equals(RecordFecthType.PUBLIC_RECORDS);

			if (!recordType.equals(RecordFecthType.ALL_RECORDS)) {
				conditions.add(criteriaBuilder.equal(
						root.<Boolean> get("isPublic"), publicFlag));
			}
			if (recordType.equals(RecordFecthType.MY_RECORDS)) {
				IPerson currentPerson = getSession().getCurrentPerson();
				conditions.add(criteriaBuilder.equal(
						root.<Person> get("owner"), currentPerson));
			}
		}

		if (conditions.size() >= 1) {
			if (conditions.size() == 1) {
				criteriaQuery.where(conditions.get(0));
			} else {
				criteriaQuery.where(criteriaBuilder.and(conditions
						.toArray(new Predicate[0])));
			}
		}

		if (sortColumn != null && sortOrder != null) {

			if (sortColumn.equals("owner")) {
				List<Order> orders = new ArrayList<Order>();
				Path<Person> owner = root.<Person> get("owner");
				if (sortOrder == SortOrder.ASCENDING) {
					orders.add(criteriaBuilder.asc(root
							.<Boolean> get("isPublic")));
					orders.add(criteriaBuilder.asc(owner.<String> get("name")));
					criteriaQuery.orderBy(orders.toArray(new Order[0]));
				} else {
					orders.add(criteriaBuilder.desc(root
							.<Boolean> get("isPublic")));
					orders.add(criteriaBuilder.desc(owner.<String> get("name")));
					criteriaQuery.orderBy(orders.toArray(new Order[0]));
				}
			} else {
				if (sortOrder == SortOrder.ASCENDING) {
					criteriaQuery.orderBy(criteriaBuilder.asc(root
							.<String> get(sortColumn)));
				} else {
					criteriaQuery.orderBy(criteriaBuilder.desc(root
							.<String> get(sortColumn)));
				}
			}
		}

		TypedQuery<T> query = em.createQuery(criteriaQuery);
		query.setFirstResult(start);
		if (max > 0) {
			query.setMaxResults(max);
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<T> listAll() {
		Query query = em.createQuery("SELECT e FROM " + clazz.getName() + " e");
		return query.getResultList();
	}


	@SuppressWarnings("unchecked")
	public T copy(Long id) throws SimpleException {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new SimpleException("Unable to clone object", e);
		}
	}

	/**
	 * Convenience method to get all objects matching a single property
	 * 
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 * @throws TooManyResultsException
	 */
	@SuppressWarnings("unchecked")
	// TODO FIX THIS
	public T getByProperty(String propName, String propValue)
			throws TooManyResultsException {
		Query query = em.createQuery(propName + " == :propValue");
		T obj = (T) query.getSingleResult();
		return obj;
	}

	public List<Long> fetchIds() {
		List<Long> ids = new ArrayList<Long>();

		List<T> objs = listAll();

		for (T obj : objs) {
			ids.add(obj.getId());
		}

		return ids;
	}

	/**
	 * This will remove a object
	 * 
	 */
	public void delete(Long id) {
		T attached = (T) em.find(clazz, id);
		EntityTransaction tx = em.getTransaction();

		try {
			if (!tx.isActive()) {
				tx.begin();
			}

			em.remove(attached);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to delete the entity with ID "
					+ id, e);
			tx.rollback();
		} finally {
			if (!tx.getRollbackOnly()) {
				tx.commit();
			}
		}
	}

	/**
	 * Bulk delete much more efficient.
	 * 
	 * JPQL bulk delete operation so that all options are deleted in single
	 * query.
	 * 
	 * @return Integer number of items deleted.
	 * @param idsToDelete
	 */
	public Integer deleteList(Set<Long> idList) {
		List<T> deleteRecords = findList(idList);

		EntityTransaction tx = em.getTransaction();
		int deletedSize = 0;
		for (T attached : deleteRecords) {
			try {
				tx.begin();
				em.remove(attached);
				deletedSize++;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unable to delete the entity with ID "
						+ attached.getId(), e);
				tx.rollback();
			} finally {
				if (tx.isActive()) {
					tx.commit();
				}
			}
		}
		return deletedSize;
	}

	/**
	 * This is slow and inefficient and should be converted to use the Cursor
	 * ability. In the future this will be removed and converted to use Cursor
	 * for data retrieval. I have marked it deprecated prematurely so that I do
	 * not forget to fix this.
	 * 
	 * TODO change findRange to use cursor syntax.
	 * 
	 * @param start
	 *            The start of how many entities to return.
	 * @param max
	 *            The maximum number of entities to return.
	 * @return List of objects in the range.
	 */
	@SuppressWarnings("unchecked")
	public List<T> findRange(int start, int max) {
		Query query = em.createQuery("SELECT e FROM " + clazz.getName() + " e");

		query.setFirstResult(start);
		query.setMaxResults(max);
		List<T> resultList = (List<T>) query.getResultList();
		resultList.size();
		return resultList;
	}


	/**
	 * Moving to the security class.
	 * 
	 * @return
	 */
	public Person getCurrentPerson() {
		return (Person) getSession().getCurrentPerson();
	}

	public List<T> search(String search) {
		throw new RuntimeException("Implement me with builder api");
	}

	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Inject
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	protected ISession getSession() {
		return session;
	}

	@Inject(optional = true)
	public void setSession(ISession session) {
		this.session = session;
	}
}