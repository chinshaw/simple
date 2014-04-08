package com.simple.original.server.service;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class RemoteLoggingService extends RemoteLoggingServiceImpl {

    
    /**
     * The directory where symbol maps are kept
     */
    private static final String defaultSymbolMapsDirectory = "/WEB-INF/deploy/virtualFactory/symbolMaps/";
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Inject
    public RemoteLoggingService(@Named("com.simple.gwt.debug.symbolmaps") String symbolMapsDir) {
        symbolMapsDir = (symbolMapsDir != null) ? symbolMapsDir : defaultSymbolMapsDirectory; 
        setSymbolMapsDirectory(symbolMapsDir);
    }
}
