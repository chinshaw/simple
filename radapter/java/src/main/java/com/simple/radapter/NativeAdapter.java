package com.simple.radapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import javax.swing.SingleSelectionModel;

import com.dyuproject.protostuff.ProtobufIOUtil;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.ParseException;
import com.simple.radapter.api.RAdapterException;
import com.simple.radapter.api.RCallbackAdapter;
import com.simple.radapter.protobuf.Rexp;

public class NativeAdapter implements IRAdapter {

    final ScheduledExecutorService singleThreadExecutor = Executors
            .newSingleThreadScheduledExecutor();

    static {
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

    private Mutex lock = new Mutex();

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
    public synchronized void connect() throws RAdapterException {
        logger.info("Calling connect");
        Runnable connect = new Runnable() {

            @Override
            public void run() {
                int configured = initR(rArgs);
                if (configured != 0) {
                    try {
                        throw new RAdapterException(
                                "Unable to connect to the R environment, R error code "
                                        + configured);
                    } catch (RAdapterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        singleThreadExecutor.submit(connect);

    }

    @Override
    public void disconnect() {
        logger.info("Calling disconnect");
        Runnable connect = new Runnable() {

            @Override
            public void run() {
                endR(0);
            }
        };

        singleThreadExecutor.submit(connect);

    }

    public Rexp exec(final String script) throws RAdapterException {

        final Callable<Rexp> exec = new Callable<Rexp>() {

            @Override
            public Rexp call() throws Exception {
                setString(".tmpCode.", script);
                byte[] packed = evalScript("eval(parse(text=.tmpCode.))");
                System.out.println("trying to convert to protobuf");

                System.out.println("packed is " + new String(packed));
                System.out.println("Length of buffer is " + packed.length);

                Rexp rexp = new Rexp();
                ProtobufIOUtil.mergeFrom(packed, rexp, Rexp.getSchema());
                return rexp;
            }

        };

        try {
            System.out.println("Calling exec");
            return singleThreadExecutor.submit(exec).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Rexp exec(File file) throws RAdapterException {
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
    public Rexp get(String var) throws RAdapterException {
        return exec(var);
    }

    public Rexp getPlot(String plotName) throws RAdapterException {
        String command = "readBin(\"" + plotName + "\", what=\"raw\", n=1e6)";
        return exec(command);
    }

    @Override
    public synchronized Rexp set(String var, Rexp rexp) throws RAdapterException {
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
            System.out.println("Writing to console stdout");
            console.writeStdOut(text);
        } else if (outputType == 1) {
            System.out.println("Writing to console stderr");
            console.writeStdErr(text);
        } else {
            System.out.println("Writing to something " + text + " out " + outputType);
        }

    }

    /**
     * JRI: R_Busy call-back from R
     * 
     * @param which
     *            state
     */
    public synchronized void jriBusy(int which) {
        System.out.println("JRI BUsy" + which);
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
        System.out.println("Calling read console");
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
        System.out.println("Jri Show Message " + message);
    }

    /**
     * JRI: R_savehistory call-back from R
     * 
     * @param filename
     *            name of the history file
     */
    public synchronized void jriSaveHistory(String filename) {
        System.out.println("JRI Save History");
    }

    public synchronized void jriFlushConsole() {
        console.flush();
    }

}
