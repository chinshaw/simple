package com.simple.orchestrator.server.event;

public class OperationReducerStateChange {

	public enum State {
		SETUP, RUN_STARTED, WRITING, CLEANUP_COMPLETE
	}

	private State state;

	public OperationReducerStateChange(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
