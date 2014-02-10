#include <Rinternals.h>
#include <Rembedded.h>
#include <Rversion.h>
#include <R_ext/Parse.h>

#include "protobuf/protoutils.h"


/* the # of arguments to R_ParseVector changed since R 2.5.0 */
#if R_VERSION < R_Version(2,5,0)
#define RPROTOSERVE_ParseVector R_ParseVector
#else
#define RPROTOSERVE_ParseVector(A,B,C) R_ParseVector(A,B,C,R_NilValue)
#endif


/* string encoding handling */
#if (R_VERSION < R_Version(2,8,0)) || (defined DISABLE_ENCODING)
#define mkRChar(X) mkChar(X)
#else
#define USE_ENCODING 1
cetype_t string_encoding = CE_NATIVE;  /* default is native */
#define mkRChar(X) mkCharCE((X), string_encoding)
#endif


// Default arguments if not specified.
char *default_args[] = {"--gui=none", "--silent"};


//  Whether or not R is initialized.
static int initialized = 0;
/*
int initR(int argc, char *argv[]) {
	if (initialized) {
		fprintf(stderr, "R is initialized, you need to either call stopR");
		return 1;
	}
	
	if (argv == NULL) {
		argc = 2;
		argv = default_args;
	}

	Rf_initEmbeddedR(argc, argv);
	initialized = 1;

	return 0;
}


int stopR() {
	initialized = 0;
	Rf_endEmbeddedR(0);
	return 0;
}
*/


REXP eval_script(const char *command) 
{
	REXP rexp = REXP__INIT;
	if (! initialized) {
		fprintf(stderr, "R has not been initialized, need to call initR");
		return rexp;
	}	
	/*
	ParseStatus status;

	SEXP sexp = R_ParseVector(mkString(command), 1, &status, R_NilValue);
	if (TYPEOF(sexp) == EXPRSXP) { // parse returns an expr vector, you want the first 
		sexp = eval(VECTOR_ELT(sexp, 0), R_GlobalEnv);
		PrintValue(sexp);
	}
	*/
	SEXP sexp = rexpress(command);
	sexpToRexp(&rexp, sexp);

	return rexp;
}


SEXP rexpress(const char* cmd)
{
	SEXP cmdSexp, cmdexpr, ans = R_NilValue;
	int i;
	ParseStatus status;
	PROTECT(cmdSexp = Rf_allocVector(STRSXP, 1));
	SET_STRING_ELT(cmdSexp, 0, Rf_mkChar(cmd));
	cmdexpr = PROTECT(R_ParseVector(cmdSexp, -1, &status, R_NilValue));
	if (status != PARSE_OK) {
		UNPROTECT(2);
		Rf_error("invalid call: %s", cmd);
		return(R_NilValue);
	}
	for(i = 0; i < Rf_length(cmdexpr); i++)
		ans = Rf_eval(VECTOR_ELT(cmdexpr, i), R_GlobalEnv);
	UNPROTECT(2);
	return(ans);
}

