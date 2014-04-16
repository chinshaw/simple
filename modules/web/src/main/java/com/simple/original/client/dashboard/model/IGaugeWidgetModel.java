package com.simple.original.client.dashboard.model;

import java.util.ArrayList;
import java.util.List;

public interface IGaugeWidgetModel extends IWidgetModel {
	
	public interface IGaugeModelRange {
		
		public abstract String getRangeName();

        public abstract void setRangeName(String rangeName);

        public abstract String getColor();

        public abstract void setColor(String color);

        public abstract Double getMinimum();

        public abstract void setMinimum(Double min);

        public abstract Double getMaximum();

        public abstract void setMaximum(Double max);
	}
	
	void setRanges(ArrayList<IGaugeModelRange> arrayList);

	List<IGaugeModelRange> getRanges();
}
