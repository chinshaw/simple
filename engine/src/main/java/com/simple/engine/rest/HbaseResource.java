package com.simple.engine.rest;

import javax.ws.rs.Path;


@Path("/hbase")
public class HbaseResource {
	
	
	@Path("/restart")
	public void restart() {
		ProcessBuilder pb = new ProcessBuilder("/opt/");
	}

}
