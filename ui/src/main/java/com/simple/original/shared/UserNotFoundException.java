/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.shared;

/**
 *
 * @author chinshaw
 */
public class UserNotFoundException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(String string) {
    super(string);
  }
}
