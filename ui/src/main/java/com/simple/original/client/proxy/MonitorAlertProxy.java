package com.simple.original.client.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.simple.domain.MonitorAlert;
import com.simple.original.api.analytics.AlertState;
import com.simple.original.server.service.locators.RequestFactoryEntityLocator;

@ProxyFor(value = MonitorAlert.class, locator = RequestFactoryEntityLocator.class)
public interface MonitorAlertProxy extends DatastoreObjectProxy {

	public static final String[] ALERT_PROPERTIES = {"alertDefinition", "factories","taskExecution"};

    /* 
     * Added this String to avoid stripping the package part from the result
	 * of Class.getName() since Class.getSimpleName() is not yet supported by GWT
	 */
	public static final String SIMPLE_NAME = "FactoryAlertProxy";

    //public FactoryProxy getFactory();

    //public void setFactory(FactoryProxy factory);

    public String getDescription();


    public AnalyticsTaskMonitorProxy getAlertDefinition() ;

    public void setAlertDefinition(AnalyticsTaskMonitorProxy alertDefinition);

    public void setDescription(String description);

    public String getResolution();

    public void setResolution(String resolution);

    public String getQuixId();

    public void setQuixId(String quixId);

    public AlertState getAlertState();

    public void setAlertState(AlertState alertState);

    public Date getCreationDate();

    public void setCreationDate(Date created);

    public Date getClosedDate();

    public void setClosedDate(Date closed);

    public String getAlertType();

    public void setAlertType(String alertType);

	public String getStatus();

	public void setStatus(String status);

	public AnalyticsTaskExecutionProxy getTaskExecution();

	public void setTaskExecution(AnalyticsTaskExecutionProxy taskExecution);

}
