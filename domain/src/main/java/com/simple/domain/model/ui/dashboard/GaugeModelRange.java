package com.simple.domain.model.ui.dashboard;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.simple.api.orchestrator.Criticality;

@Embeddable
public class GaugeModelRange {

	/**
	 * The name of the range for example Minimum, Maximum etc.
	 */
	private String rangeName;

	/**
	 * The minimum value for the range.
	 */
	@Column(name = "min")
	private Double min;

	/**
	 * This maximum value for the range
	 */
	@Column(name = "max")
	private Double max;

	/**
	 * The hex value of the color.
	 */
	@NotNull(message = "Gauge widget ranges must all have a selected color")
	private String color = "grey";

	public GaugeModelRange() {
	}

	public GaugeModelRange(Double min, Double max, String color) {
		this();
		this.min = min;
		this.max = max;
		this.color = color;
	}

	public GaugeModelRange(Double min, Double max, Criticality criticality) {
		this(min, max, getCritilityColor(criticality));
	}

	public GaugeModelRange(NumberRange range) {
		this(range.getMinimum(), range.getMaximum(), range.getCriticality());
	}

	/**
	 * Getter for the name of the range
	 */
	public String getRangeName() {
		return rangeName;
	}

	/**
	 * Setter for the name of the range.
	 * 
	 * @param rangeName
	 */
	public void setRangeName(String rangeName) {
		this.rangeName = rangeName;
	}

	public Double getMinimum() {
		return min;
	}

	public void setMinimum(Double min) {
		this.min = min;
	}

	public Double getMaximum() {
		return max;
	}

	public void setMaximum(Double max) {
		this.max = max;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	/**
	 * TODO THIS NEEDS TO GO SOMEWHERE WHERE IT CAN BE SHARED WITH THE CLIENT,
	 * there is a duplicate in the GaugeModelRangesEditor class also and they
	 * may get out of sync
	 * 
	 * @param criticality
	 * @return
	 */
	public static String getCritilityColor(Criticality criticality) {
		if (criticality == null) {
			return "grey";
		}

		switch (criticality) {
		case NORMAL:
			return "green";
		case WARNING:
			return "yellow";
		case CRITICAL:
			return "red";
		default:
			return "grey";
		}
	}
}
