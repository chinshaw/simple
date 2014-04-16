package com.simple.original.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.simple.original.server.service.Notification;
import com.simple.original.server.service.NotificationService;
import com.simple.original.server.service.Notification.ParameterMap;

public class NotificationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7762112233679634953L;

	private static final Logger logger = Logger.getLogger(NotificationServlet.class.getName());
	
    public void doPost(final HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	    logger.info("Received notification");
	    
	    Notification notification =  Notification.fromParameterMap(new ParameterMap() {

            @Override
            public String get(String param) {
                return request.getParameter(param);
            }
	        
	    });
	    
	    logger.info("Notification looks like " + notification.toString());
	    
	    logger.info("Processing notification");
	    NotificationService.getInstance().processNotification(notification);
	    logger.info("Successfully processed notification");
	}
}