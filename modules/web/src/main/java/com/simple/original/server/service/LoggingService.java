package com.simple.original.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.google.inject.name.Named;

public class LoggingService {

	@Named("com.simple.logging.server.log.location")
	private static String serverLogLocation;
	

	@Named("com.simple.logging.r.log.location")
	private static String rLogLocation;
	
	@Named("com.simple.logging.taskengine.log.location")
	private static String taskEngineLogLocation;
	
	public LoggingService() {
		
	}
	
    public static final String getServerLog(int lineCount) throws FileNotFoundException {
        if (serverLogLocation == null) {
            throw new FileNotFoundException("Couldn't get file location for server log");
        }
        return tailLog(serverLogLocation, lineCount);
    }

    public static final String getRLog(int lineCount) throws FileNotFoundException {
        if (rLogLocation == null) {
            throw new FileNotFoundException("Couldn't get file location for r log");
        }
        return tailLog(serverLogLocation, lineCount);
    }

    public static final String getTaskEngineLog(int lineCount) throws FileNotFoundException {
        if (taskEngineLogLocation == null) {
            throw new FileNotFoundException("Couldn't get file location for task engine log");
        }
        return tailLog(serverLogLocation, lineCount);
    }

    private static String tailLog(String filePath, int lineCount) throws FileNotFoundException {
        File serverLogFile = new File(filePath);
        RandomAccessFile raf = new RandomAccessFile(serverLogFile, "r");
        return tail(raf, lineCount);
    }

    private static String tail(RandomAccessFile raf, int lineCount) {
        int BUFFERSIZE = 1024;
        long pos;
        long endPos;
        long lastPos;
        int numOfLines = 0;
        byte[] buffer = new byte[BUFFERSIZE];
        StringBuffer sb = new StringBuffer();
        try {
            endPos = raf.length();
            lastPos = endPos;

            // Check for non-empty file
            // Check for newline at EOF
            if (endPos > 0) {
                byte[] oneByte = new byte[1];
                raf.seek(endPos - 1);
                raf.read(oneByte);
                if ((char) oneByte[0] != '\n') {
                    numOfLines++;
                }
            }

            do {
                // seek back BUFFERSIZE bytes
                // if length of the file if less then BUFFERSIZE start from BOF
                pos = 0;
                if ((lastPos - BUFFERSIZE) > 0) {
                    pos = lastPos - BUFFERSIZE;
                }
                raf.seek(pos);
                // If less then BUFFERSIZE avaliable read the remaining bytes
                if ((lastPos - pos) < BUFFERSIZE) {
                    int remainer = (int) (lastPos - pos);
                    buffer = new byte[remainer];
                }
                raf.readFully(buffer);
                // in the buffer seek back for newlines
                for (int i = buffer.length - 1; i >= 0; i--) {
                    if ((char) buffer[i] == '\n') {
                        numOfLines++;
                        // break if we have last n lines
                        if (numOfLines > lineCount) {
                            pos += (i + 1);
                            break;
                        }
                    }
                }
                // reset last postion
                lastPos = pos;
            } while ((numOfLines <= lineCount) && (pos != 0));

            // print last n line starting from last postion
            for (pos = lastPos; pos < endPos; pos += buffer.length) {
                raf.seek(pos);
                if ((endPos - pos) < BUFFERSIZE) {
                    int remainer = (int) (endPos - pos);
                    buffer = new byte[remainer];
                }
                raf.readFully(buffer);
                sb.append(new String(buffer));
            }

        } catch (FileNotFoundException e) {
            sb = null;
        } catch (IOException e) {
            e.printStackTrace();
            sb = null;
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (sb == null) {
            return null;
        }
        // String[] tmp = { sb.toString(), info };
        return sb.toString();
    }

}
