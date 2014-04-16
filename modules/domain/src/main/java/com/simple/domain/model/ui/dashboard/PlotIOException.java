package com.simple.domain.model.ui.dashboard;

/**
 * Typically used when a problem occurs saving or deleting a plot to the file
 * system. Things to check when receiving this type of exception are filesystem permissions
 * or that the path actually exists.
 * 
 * @author chinshaw
 */
public class PlotIOException extends RuntimeException {

    /**
     * Serialization Id
     */
    private static final long serialVersionUID = -630011143221213457L;

    /**
     * Plot exception with a message.
     * 
     * @param message
     */
    public PlotIOException(String message) {
        super(message);
    }

    /**
     * Plot exception with message and throwable
     * 
     * @param message
     * @param throwable
     */
    public PlotIOException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
