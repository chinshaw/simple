package com.simple.original.client.proxy;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.requestfactory.shared.EntityProxyId;
import com.google.web.bindery.requestfactory.shared.impl.BaseProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.EntityProxyCategory;
import com.google.web.bindery.requestfactory.shared.impl.ValueProxyCategory;


@AutoBeanFactory.Category(value = { EntityProxyCategory.class, ValueProxyCategory.class, BaseProxyCategory.class })
@AutoBeanFactory.NoWrap(EntityProxyId.class)
public interface AnalyticsOperationInputBeanFactory extends AutoBeanFactory {
    AutoBean<InputCollectionProxy> inputs();

    AutoBean<AnalyticsOperationInputProxy> input();
    
    AutoBean<UIDateInputModelProxy> dateInput();
    
    AutoBean<UIUserInputModelProxy> stringInput();
}
