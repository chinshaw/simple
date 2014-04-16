/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.reporting;

/**
 *
 * @author chinshaw
 */
public class ReportGenerationException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ReportGenerationException() {
  }

  public ReportGenerationException(String message) {
    super(message);
  }
  
  public ReportGenerationException(String message, Throwable throwable) {
	  super(message, throwable);
  }
}
