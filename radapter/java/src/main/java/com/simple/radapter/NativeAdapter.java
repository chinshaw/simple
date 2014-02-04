package com.simple.radapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import com.simple.radapter.api.IEngine;
import com.simple.radapter.api.IRAdapter;
import com.simple.radapter.api.IRexp;
import com.simple.radapter.api.RAdapterException;

public class NativeAdapter implements IRAdapter {

	private static final Logger logger = Logger.getLogger(NativeAdapter.class
			.getName());

	private IEngine engine;

	private boolean started;

	public NativeAdapter() {
	}

	public NativeAdapter(IEngine engine) {
		this.engine = engine;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() throws RAdapterException {
		if (started) {
			throw new RAdapterException("Only one connection at a time per jvm");
		}
		engine.startSession();
		started = true;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public IRexp<?> exec(String command) throws RAdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IRexp<?> execScript(String script) throws RAdapterException {
		setString(".tmpCode.", script);
		return eval("eval(parse(text=.tmpCode.))");
	}

	@Override
	public IRexp<?> execScript(File file) throws RAdapterException {
		if (file == null) {
			throw new IllegalArgumentException("File cannot be null");
		}

		logger.fine("Reading file " + file.getPath());

		String scriptCode = null;

		FileReader reader = null;
		BufferedReader br = null;
		try {
			try {
				reader = new FileReader(file);
				br = new BufferedReader(reader);

				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append(System.lineSeparator());
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

		return execScript(scriptCode);
	}
	
	public <T extends IRexp<?>> T getTypedRexp(String name) throws RAdapterException {
		return (T) getRexp(name);
	}
	
	private IRexp<?> getRexp(String name) throws RAdapterException {
		long parsed = engine.parse(name, 1);
		if (parsed != 0) {
			long evaled = engine.eval(parsed, IRAdapter.GLOBAL_ENVIRONMENT);
			if (evaled == 0) {
				throw new RAdapterException("Unable to retrieve variable from workspace: " + name);
			}
			IRexp<?> rexp = RexpUtils.convert(engine, evaled);
			return rexp;
		}
		throw new RAdapterException("Invalid rexp " + name);
	}

	@Override
	public void setString(String var, String value) {
		long exp = engine.setString(value);
		engine.assign("test1", exp, IRAdapter.GLOBAL_ENVIRONMENT);
	}

	@Override
	public String getString(String var) throws RAdapterException {
		return engine.getString(var);

	}

	private IRexp<?> eval(String code) throws RAdapterException {
		long expression = engine.parse(code, 1);

		if (expression == 0) {
			throw new RAdapterException("Unable to parse code");
		}

		expression = engine.eval(expression, IRAdapter.GLOBAL_ENVIRONMENT);
		return RexpUtils.convert(engine, expression);
	}

}