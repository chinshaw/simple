package com.simple.original.client.dashboard.model;



public interface IWidgetModel extends IHasWidgetStyle {
	
	public String getWidgetType();

	public String getDescription();

	public void setDescription(String description);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public Long getMetricId();
	
	public void setMetricId(Long id);
	
	public String toJson();
	
	public String getType();
}
