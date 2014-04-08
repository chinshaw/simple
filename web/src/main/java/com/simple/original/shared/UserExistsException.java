/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.simple.original.shared;

/**
 *
 * @author chinshaw
 */
public class UserExistsException extends Exception {


  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public UserExistsException() {
    super();
  }

  public UserExistsException(String string) {
    super(string);
  }
}
