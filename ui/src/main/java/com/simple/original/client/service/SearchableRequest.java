package com.simple.original.client.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.simple.original.api.domain.RecordFecthType;
import com.simple.original.api.domain.SortOrder;

public interface SearchableRequest<T extends EntityProxy> extends RequestContext {
	Request<List<T>> search(String search);

	Request<List<T>> search(int start, int max, RecordFecthType recordType, String searchText, String searchColumn, String sortColumn, SortOrder sortOrder);
}
