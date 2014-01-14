package com.simple.domain.model.ui.dashboard;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Size;

import com.simple.domain.model.RequestFactoryEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Widget extends RequestFactoryEntity {
    
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 5141586126567140677L;

    private String title;

	/**
	 * This is the description of the widget and is essentially
	 * used for it's title in a gwt widget. Title in gwt is the 
	 * popup 
	 */
	@Size(max = 256, message = "Description cannot exceed 256 characters")
    private String description;
    
	private String backgroundColor;
	
	public Widget() {
	}
	
	public Widget(String title) {
		this.title = title;
	}
	
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getBackgroundColor() {
    	return backgroundColor;
    }
    
    public void setBackgroundColor(String backgroundColor) {
    	this.backgroundColor = backgroundColor;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public Widget clone() throws CloneNotSupportedException {
    	Widget clone = (Widget) super.clone();
    	return clone;
    }
}