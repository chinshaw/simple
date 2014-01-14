package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.simple.domain.model.ui.dashboard.Dashboard;
import com.simple.domain.model.ui.dashboard.Widget;

/**
 * Mixin annotations for the {@link Widget} class.
 * @author chinshaw
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = Dashboard.class, name = "Dashboard"),
    })
public interface WidgetMixin {

}
