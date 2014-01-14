package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.model.ui.AnalyticsOperationInput;
import com.simple.domain.model.ui.ComplexInput;
import com.simple.domain.model.ui.DateInput;
import com.simple.domain.model.ui.StringInput;


/**
 * Mixin class for annotating class {@link AnalyticsOperationInput}
 * @author chinshaw
 *
 *
 *
 * This is the annotations for version svn exported version 677
 * Version 677
 * @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
 * @JsonSubTypes({ @JsonSubTypes.Type(value = StringInput.class, name = "UIUserInputModel"),
 *   @JsonSubTypes.Type(value = UIDateInputModel.class, name = "UIDateInputModel"),
 *   @JsonSubTypes.Type(value = UIComplexInputModel.class, name = "UIComplexInputModel") })
 * 
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @JsonSubTypes.Type(value = StringInput.class, name = "UIUserInputModel"),
  @JsonSubTypes.Type(value = DateInput.class, name = "UIDateInputModel"),
  @JsonSubTypes.Type(value = ComplexInput.class, name = "UIComplexInputModel") })
public interface AnalyticsOperationInputMixin {

    @JsonIgnore
    public String getValueAsString();
}
