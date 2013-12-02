package com.simple.original.security;

import com.simple.original.api.exceptions.SimpleException;

public interface IPermissions {

    
    public abstract boolean hasTaskLimitExceeded(Long userId) throws SimpleException;
}
