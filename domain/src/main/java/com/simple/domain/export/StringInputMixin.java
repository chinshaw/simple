package com.simple.domain.export;

import com.fasterxml.jackson.annotation.JsonSetter;

public interface StringInputMixin {

    
    @JsonSetter("defaultValue")
    public void setValue(String value);
}
