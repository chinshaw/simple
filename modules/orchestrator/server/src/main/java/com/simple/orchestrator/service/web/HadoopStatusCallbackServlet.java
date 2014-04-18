package com.simple.orchestrator.service.web;

import java.io.IOException;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.name.Named;

@WebServlet(urlPatterns = { "/h/v2/status", "/h/v2/status" })
public class HadoopStatusCallbackServlet extends HttpServlet {

	private static final String JOB_ID_PARAMETER = "jobid";

	private static final String STATUS_PARAMETER = "status";

	/**
	 * 
	 */
	private static final long serialVersionUID = -9071615085127018327L;

	private Session session;
	
	private Destination destination;

	public HadoopStatusCallbackServlet() {

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String jobid = req.getParameter(JOB_ID_PARAMETER);
		String jobstatus = req.getParameter(STATUS_PARAMETER);

		if (jobid == null) {
			throw new ServletException("job id is null");
		}
		if (jobstatus == null) {
			throw new ServletException("job status is null");
		}

		try {
			MessageProducer producer = session.createProducer(destination);

		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}

	@Inject
	public void setQueue(Session session) {
		this.session = session;
	}

	@Inject
	public void setDestination(
			@Named("job.status.destination") Destination destination) {
		this.destination = destination;
	}
}
