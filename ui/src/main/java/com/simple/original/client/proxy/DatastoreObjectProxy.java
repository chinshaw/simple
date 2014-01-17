/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.SkipInterfaceValidation;
import com.simple.domain.model.DatastoreObject;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

/**
 * 
 * @author chris
 */
// TODO The @SkipIntefaceValidation needs to be removed but there is an extended enity proxy somewhere that does not extend
// the DatastoreObjectImpl which is breaking this interface.
@SkipInterfaceValidation
@ProxyFor(value = DatastoreObject.class, locator = RequestFactoryEntityLocator.class)
public interface DatastoreObjectProxy extends EntityProxy {

	public Long getId();

    public Integer getVersion();
}