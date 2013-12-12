package com.simple.original.client.service;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.SkipInterfaceValidation;

@SkipInterfaceValidation
public interface PaginationRequest<T extends EntityProxy> extends RequestContext {
    Request<List<T>> findRange(int start, int end);
}
