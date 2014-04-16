package com.simple.domain.dao;

import java.util.List;
import java.util.Set;

import com.simple.api.domain.RecordFecthType;
import com.simple.api.domain.SortOrder;
import com.simple.api.exceptions.DomainException;
import com.simple.api.exceptions.SimpleException;


public interface IDaoRequest<T> {

	
	T find(Long id);

	/**
	 * Save the object and return the key of the object.
	 * 
	 * @param obj
	 * @return
	 */
	T copy(Long id) throws SimpleException;
	
	Long save(T obj) throws DomainException;

	T saveAndReturn(T obj) throws DomainException;

	List<T> listAll();

	void delete(Long id);

	Integer deleteList(Set<Long> idsToDelete);

	List<Long> fetchIds();
	
	List<T> findList(Set<Long> ids);

	List<T> search(int start, int max, RecordFecthType recordType, String searchText, String searchColumn, String sortColumn, SortOrder sortOrder);
}
