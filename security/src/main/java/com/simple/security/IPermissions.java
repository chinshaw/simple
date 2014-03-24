package com.simple.security;

import com.simple.api.exceptions.SimpleException;

public interface IPermissions {

    
    public abstract boolean hasTaskLimitExceeded(Long userId) throws SimpleException;
}
