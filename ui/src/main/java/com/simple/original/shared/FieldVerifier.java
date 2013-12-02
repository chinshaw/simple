package com.simple.original.shared;


/**
 * <p>
 * FieldVerifier validates that the name the user enters is valid.
 * </p>
 * <p>
 * This class is in the <code>shared</code> package because we use it in both
 * the client code and on the server. On the client, we verify that the name is
 * valid before sending an RPC request so the user doesn't have to wait for a
 * network round trip to get feedback. On the server, we verify that the name is
 * correct to ensure that the input is correct regardless of where the RPC
 * originates.
 * </p>
 * <p>
 * When creating a class that is used on both the client and the server, be sure
 * that all code is translatable and does not use native JavaScript. Code that
 * is note translatable (such as code that interacts with a database or the file
 * system) cannot be compiled into client side JavaScript. Code that uses native
 * JavaScript (such as Widgets) cannot be run on the server.
 * </p>
 */
public class FieldVerifier {

  
  public static boolean isValidEmailAddress(String emailAddress)
  {
    // a null string is invalid
    if ( emailAddress == null ) {
      return false;
    }
    
    if ( emailAddress.length() < 5 ) {
      return false;
    }

    // a string without a "@" is an invalid email address
    if ( emailAddress.indexOf("@") < 0 ) {
      return false;
    }

    // a string without a "."  is an invalid email address
    if ( emailAddress.indexOf(".") < 0 ) {
      return false;
    }
    
    return true;
  }
  
  public static boolean isValidPassword(String password) {
    
    if (password == null) {
      return false;
    }
    
    if ( password.length() < 5) {
      return false;
    }
    
    return true;
  }
  
  public static boolean isValidName(String userName) {
    
    // Check to see that the entered a first and last name.
    if (userName.indexOf(" ") < 0 ) {
      return false;
    }
    
    // Could be Tu Vu but 4 chars is the least we will accept.
    if (userName.length() < 4) {
      return false;
    }
    return true;
  }
}
