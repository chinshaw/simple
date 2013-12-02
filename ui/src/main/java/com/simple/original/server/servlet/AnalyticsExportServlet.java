package com.simple.original.server.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.simple.domain.metric.MetricMatrix;
import com.simple.original.shared.AnalyticsExport;
import com.simple.original.shared.AnalyticsExport.ExportType;
import com.simple.reporting.ExportService;

public class AnalyticsExportServlet extends HttpServlet {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private static final String TMP_DIR_PATH = System.getProperty("java.io.tmpdir");
	private File tmpDir;
	
	private EntityManager em;
	
	@Inject
	public AnalyticsExportServlet(EntityManager em) {
		this.em = em;
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if (!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		String exportTypeParam = req.getParameter(AnalyticsExport.EXPORT_TYPE);

		// Future use.
		@SuppressWarnings("unused")
		ExportType type = null;
		
		try {
			ExportType.valueOf(exportTypeParam);
		} catch (IllegalArgumentException ill) {
			sendError(resp, "exportType was not specified");
			return;
		}
		
		String exportId = req.getParameter(AnalyticsExport.EXPORT_ID);
		if (exportId == null) {
			sendError(resp,"exportId was not specified" );
			return;
		}

		MetricMatrix metricMatrix = em.find(MetricMatrix.class, Long.parseLong(exportId));
		
		if (metricMatrix == null) {
			sendError(resp, "invalid exportId was specified");
			return;
		}

		ExportService service = new ExportService();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.DD");
		String date = dateFormat.format(new Date());

		byte[] xlsData = null;

		try {
			xlsData = service.exportMetricMatrixToXls(metricMatrix);
			if (xlsData.length == 0) {
				sendError(resp, "no data was retrieved");
				return;
			}
			
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment;filename=MetricTable-" + date + ".xlsx");
			resp.getOutputStream().write(xlsData);
			
		} catch (IOException e) {
			sendError(resp, "Error occurred while exporting matrix " + e.getMessage());
		}
	}
	
	
	/**
	 * Handles io exception also for cleanliness.
	 * @param resp
	 * @param errorMessage
	 */
	private void sendError(HttpServletResponse resp, String errorMessage) {
		try {
			resp.sendError(500, errorMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		sendError(resp, "HTTP POST NOT ALLOWED");
	}
}
