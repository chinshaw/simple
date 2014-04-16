package com.simple.original.client.place;

public class PlaceException extends Exception  {

    /**
     * 
     */
    private static final long serialVersionUID = -8772532972903784852L;

    public PlaceException() {
        super();
    }

    public PlaceException(String message, Throwable thr) {
        super(message, thr);
    }

    public PlaceException(String  message) {
        super(message);
    }

    public PlaceException(Throwable thr) {
        super(thr);
    }

}
