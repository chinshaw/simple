package com.simple.original.api.analytics;

import java.util.List;

public interface IHasViolations {
    
    public List<? extends IViolation> getViolations();
}
