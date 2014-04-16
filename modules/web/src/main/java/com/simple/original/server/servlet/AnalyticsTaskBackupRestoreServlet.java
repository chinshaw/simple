package com.simple.original.server.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.inject.Inject;
import com.simple.domain.dao.AnalyticsTaskDao;

public class AnalyticsTaskBackupRestoreServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final String TMP_DIR_PATH = System.getProperty("java.io.tmpdir");
    private File tmpDir;
    
    private final AnalyticsTaskDao taskDao;
    
    @Inject
    public AnalyticsTaskBackupRestoreServlet(AnalyticsTaskDao taskDao) {
    	this.taskDao = taskDao;
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.DD");
        String date = dateFormat.format(new Date());
        
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=allAnalyticsTasks-" + date + ".xml");

        try {
            resp.getWriter().write(taskDao.exportAnalyticsTasks());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletFileUpload upload = new ServletFileUpload();

        try{
            FileItemIterator iter = upload.getItemIterator(req);
            //ByteArrayOutputStream out = new ByteArrayOutputStream();
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                
                taskDao.importAnalyticsTasks(stream);

            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
