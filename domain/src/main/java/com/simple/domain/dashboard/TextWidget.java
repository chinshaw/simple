package com.simple.domain.dashboard;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "dashboard_textwidget")
public class TextWidget extends Widget {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -6570619767440480427L;

    private String text;

    public TextWidget() {
    	
    }
    
    public TextWidget(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}