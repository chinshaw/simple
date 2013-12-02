/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.api.exceptions;

/**
 *
 * @author chinshaw
 */
public class NotAuthenticatedException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public NotAuthenticatedException() {
    super("Unauthenticated request");
  }

  public NotAuthenticatedException(String message) {
    super(message);
  }

}
