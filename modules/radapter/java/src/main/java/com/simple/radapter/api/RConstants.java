package com.simple.radapter.api;

public class RConstants {
	/* internal SEXP types in R - taken directly from Rinternals.h */
	
	/**
	 * Nill or null.
	 */
	public static final int NILSXP = 0; 

	/**
	 * Symbol
	 */
	public static final int SYMSXP = 1; 

	/**
	 * List of dotted pairs
	 */
	public static final int LISTSXP = 2;

	/**
	 * Closures
	 */
	public static final int CLOSXP = 3; /* closures */

	/**
	 * Environment expression
	 */
	public static final int ENVSXP = 4; 

	public static final int PROMSXP = 5; /*
										 * promises: [un]evaluated closure
										 * arguments
										 */

	public static final int LANGSXP = 6; /*
										 * language constructs (special lists)
										 */

	public static final int SPECIALSXP = 7; /* special forms */

	public static final int BUILTINSXP = 8; /* builtin non-special forms */

	public static final int CHARSXP = 9; /*
										 * "scalar" string type (internal only)
										 */

	public static final int LGLSXP = 10; /* logical vectors */

	public static final int INTSXP = 13; /* integer vectors */

	public static final int REALSXP = 14; /* real variables */

	public static final int CPLXSXP = 15; /* complex variables */

	public static final int STRSXP = 16; /* string vectors */

	public static final int DOTSXP = 17; /* dot-dot-dot object */

	public static final int ANYSXP = 18; /* make "any" args work */

    public static final int VECSXP = 19; /* generic vectors */

    public static final int EXPRSXP = 20; /* expressions vectors */

    public static final int BCODESXP = 21; /* byte code */

    public static final int EXTPTRSXP = 22; /* external pointer */

    public static final int WEAKREFSXP = 23; /* weak reference */

    public static final int RAWSXP = 24; /* raw bytes */

    public static final int S4SXP = 25; /* S4 object */

    public static final int FUNSXP = 99; /* Closure or Builtin */

	
}
