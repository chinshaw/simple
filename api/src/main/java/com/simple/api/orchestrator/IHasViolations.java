package com.simple.api.orchestrator;

import java.util.List;

public interface IHasViolations {
    
    public List<? extends IViolation> getViolations();
}
