package com.simple.domain.export;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.simple.domain.model.ui.DateInput;

/**
 * Mixin annotations for the {@link DateInput} class
 * @author chinshaw
 *
 */
@JsonIgnoreProperties(value = {"value"})
public interface DateInputMixin {

    
    
    @JsonSetter("defaultValue")
    public void setValue(Date value);
}
