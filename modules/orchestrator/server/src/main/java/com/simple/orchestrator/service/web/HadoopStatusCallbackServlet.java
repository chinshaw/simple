package com.simple.orchestrator.service.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.simple.orchestrator.service.hadoop.job.JobCompletionEvent;

@WebServlet(urlPatterns = { "/h/v2/status", "/h/v2/status" })
public class HadoopStatusCallbackServlet extends HttpServlet {

	private static final String JOB_ID_PARAMETER = "jobid";

	private static final String STATUS_PARAMETER = "status";

	/**
	 * 
	 */
	private static final long serialVersionUID = -9071615085127018327L;
	
	private EventBus eventBus;

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

		JobCompletionEvent jobCompletion = new JobCompletionEvent(jobid, jobstatus);
		eventBus.post(jobCompletion);
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
