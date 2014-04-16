package com.simple.domain.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;


/**
 * This is a list of cell phone provider  that we are subscribed to.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CellPhoneProviders {
	
	/**
	 * This is a list of cell phone provider  that we are subscribed to.
	 */
	@XmlElement(name = "cellPhoneProvider")
	private List<CellPhoneProvider> cellPhoneProviders;

	public List<CellPhoneProvider> getCellPhoneProviders() {
		return cellPhoneProviders;
	}

	public void setCellPhoneProvider(List<CellPhoneProvider> cellPhoneProviders) {
		this.cellPhoneProviders = cellPhoneProviders;
	}

}
