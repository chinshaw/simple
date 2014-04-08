package com.simple.original.server.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SoftwareUpdateServlet extends HttpServlet {

	@Inject
	@Named("com.simple.app.software.upgrade.dir")
	private static String softwareUpgradeStagingDirectory;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7762112233679634953L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();

		// Get the staging directory and create it if it does not exist.
		File stagingDir = new File(softwareUpgradeStagingDirectory);
		if (!stagingDir.exists()) {
			if (!stagingDir.mkdirs()) {
				throw new RuntimeException(
						"We were unable to create the staging directory for softare updates");
			}
		}

		FileOutputStream out = null;

		try {
			FileItemIterator iter = upload.getItemIterator(request);
			out = new FileOutputStream(stagingDir.getAbsolutePath()
					+ "/upgrade.package.tgz");

			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				InputStream stream = null;

				try {
					stream = item.openStream();

					int len;
					byte[] buffer = new byte[8192];
					while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, len);
					}
				} finally {
					if (stream != null) {
						stream.close();
					}
				}
			}

			System.out.println("Executing script");
			// Run the install script

			
			
			Process process = new ProcessBuilder("/bin/sh", "/usr/local/virtualfactory/upgrades/install").start();
			
			
			BufferedReader stdout = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line = null;
			while ((line = stdout.readLine()) != null) {
				System.out.println(line);
			}

			while ((line = stderr.readLine()) != null) {
				System.out.println(line);
			}
			int ret = process.waitFor();

			System.out.println("Return was " + ret);
			
			stdout.close();
			stderr.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}