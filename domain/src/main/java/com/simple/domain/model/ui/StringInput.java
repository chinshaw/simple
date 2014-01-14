package com.simple.domain.model.ui;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.simple.original.api.analytics.IStringInputEntity;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "string_input")
public class StringInput extends AnalyticsOperationInput implements IStringInputEntity {
    
    /**
     * Serialization ID
     */
    private static final long serialVersionUID = 8811757733919511473L;
    
    /**
     * The string value of this input.
     */
    @Column(name = "string_value")
    private String value;

    /**
     * This is a list of possible inputs. This will translate into a list of
     * hard defined options that the user can specify.
     */
    @ElementCollection()
    private List<String> definedInputs;

    public StringInput() {
        definedInputs = new ArrayList<String>();
    }

    public StringInput(String inputName, String value) {
        super(inputName);
        this.value = value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.simple.original.server.domain.IStringInput#getDefinedInputs()
     */
    @XmlElementWrapper
    @XmlElement(name = "definedInput")
    public List<String> getDefinedInputs() {
        return definedInputs;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.simple.original.server.domain.IStringInput#setDefinedInputs(java.util.List)
     */
    public void setDefinedInputs(List<String> definedInputs) {
        this.definedInputs = definedInputs;
    }

    public StringInput clone() throws CloneNotSupportedException {
        StringInput clone = (StringInput) super.clone();

        clone.setDefinedInputs(new ArrayList<String>());
        for (String definedInput : definedInputs) {
            clone.definedInputs.add(definedInput);
        }
        return clone;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getValueAsString() {
        return value;
    }

}
