package com.simple.security.api;

import com.simple.api.orchestrator.IPerson;

public interface IPersonProvider {

	
	public IPerson findByEmail(String id);
}
