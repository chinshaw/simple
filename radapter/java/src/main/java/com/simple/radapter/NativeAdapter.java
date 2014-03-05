package com.simple.radapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.ParseException;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.api.RCallbackAdapter;
import com.simple.radapter.protobuf.REXP;

public class NativeAdapter implements IRAdapter {

    static  {
        try {
            System.loadLibrary("radapter");
        } catch (UnsatisfiedLinkError e) {
            String searchPath = System.getProperty("java.library.path");
            throw new RuntimeException("Unable to find native library in the following paths "
                    + searchPath, e);
        }
    }

    // Member variables

    private static final Logger logger = Logger.getLogger(NativeAdapter.class.getName());

    public static final int GLOBAL_ENVIRONMENT = 0;

    /**
     * Is it started, R is single threaded
     */
    private static boolean started = false;

    public static final String[] DEFAULT_R_ARGS = { "--vanilla" };

    private final String[] rArgs;

    private RCallbackAdapter console;

    public NativeAdapter() {
        this(DEFAULT_R_ARGS);
    }

    public NativeAdapter(String[] rArgs) {
        this.rArgs = rArgs;
        console = new SimpleCallbackAdapter();
    }

    /**
     * {@inheritDoc}
     * 
     * Calls setup on the r environment, which is required to be run before
     * running any R commands.
     */
    @Override
    public void connect() throws RAdapterException {
        int configured = initR(rArgs);
        if (configured != 0) {
            throw new RAdapterException("Unable to connect to the R environment, R error code "
                    + configured);
        }
    }

    @Override
    public void disconnect() {
        endR(0);
    }

    public REXP exec(String script) throws RAdapterException {
        setString(".tmpCode.", script);
        byte[] packed = evalScript("eval(parse(text=.tmpCode.))");

        
        REXP rexp = new REXP();
        ProtobufIOUtil.mergeFrom(packed, rexp, REXP.getSchema());
        return rexp;
    }

    @Override
    public REXP exec(File file) throws RAdapterException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        String scriptCode = null;

        FileReader reader = null;
        BufferedReader br = null;
        try {
            try {
                logger.fine("executing r script " + file.getPath());
                reader = new FileReader(file);
                br = new BufferedReader(reader);

                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                scriptCode = sb.toString();

            } catch (Exception e) {
                if (e instanceof IOException) {
                    throw new Exception("Unable to read the file " + file.getAbsolutePath());
                }
            } finally {
                reader.close();
                br.close();
            }
        } catch (Exception e) {
            throw new RAdapterException("Unable to parse your script ", e);
        }

        return exec(scriptCode);
    }

    public void setString(String var, String value) {
        long exp = jniSetString(value);
        if (exp == 0) {
            throw new ParseException();
        }
        jniAssign(var, exp, GLOBAL_ENVIRONMENT);
    }

    /**
     * Used to return a rexp object.
     * 
     * @throws RAdapterException
     * @throws InvalidProtocolBufferException
     */
    @Override
    public REXP get(String var) throws RAdapterException {
        return exec(var);
    }
    

    public REXP getPlot(String plotName) throws RAdapterException {
		String command = "readBin(\"" + plotName
				+ "\", what=\"raw\", n=1e6)";
		return exec(command);
    }
    
    @Override
    public synchronized REXP set(String var, REXP rexp) throws RAdapterException {
        // TODO Auto-generated method stub
        return null;
    }


    private synchronized native int initR(String[] rArgs);

    private synchronized native void endR(int flag);

    public synchronized native byte[] evalScript(String command);

    public synchronized native long jniSetString(String string);

    public synchronized native boolean jniAssign(String name, long exp, long environment);

    /**
     * JRI: R_WriteConsole call-back from R
     * 
     * @param text
     *            text to disply
     */
    public synchronized void jriWriteConsole(String text, int outputType) {
        if (outputType == 0) { // stdout
            console.writeStdOut(text);
        } else {
            console.writeStdErr(text);
        }
    }

    /**
     * JRI: R_Busy call-back from R
     * 
     * @param which
     *            state
     */
    public synchronized void jriBusy(int which) {
    }

    /**
     * JRI: R_ReadConsole call-back from R.
     * 
     * @param prompt
     *            prompt to display before waiting for the input.<br>
     *            <i>Note:</i> implementations should block for input. Returning
     *            immediately is usually a bad idea, because the loop will be
     *            cycling.
     * @param addToHistory
     *            flag specifying whether the entered contents should be added
     *            to history
     * @return content entered by the user. Returning <code>null</code>
     *         corresponds to an EOF and usually causes R to exit (as in
     *         <code>q()</doce>).
     */
    public synchronized String jriReadConsole(String prompt, int addToHistory) {

        // System.out.println("Rengine.jreReadConsole BEGIN "
        // + Thread.currentThread());
        return null;
    }

    /**
     * JRI: R_ShowMessage call-back from R
     * 
     * @param message
     *            message
     */
    public synchronized void jriShowMessage(String message) {
    }

    /**
     * JRI: R_savehistory call-back from R
     * 
     * @param filename
     *            name of the history file
     */
    public synchronized void jriSaveHistory(String filename) {
    }

}
