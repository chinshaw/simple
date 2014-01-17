package com.simple.original.client.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.simple.domain.model.AlertViolationData;

@ProxyFor(value = AlertViolationData.class)
public interface AlertViolationDataProxy  extends ValueProxy {

	public String getRuleName();

	public String getChartStatistics();

	public String getStartDate();

	public String getPCenter();

	public String getUCL();

	public String getLCL();

	public void setRuleName(String ruleName);

	public void setChartStatistics(String chartStatistics);

	public void setStartDate(String startDate);
}