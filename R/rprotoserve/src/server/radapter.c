#include "radapter.h"
#include <R_ext/Parse.h>

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

SEXP eval_script(const char *command) {
	if (! initialized) {
		fprintf(stderr, "R has not been initialized, need to call initR");
		return NULL;
	}	

	SEXP e, result;
	ParseStatus status;

	result = R_ParseVector(mkString(command), 1, &status, R_NilValue);
	if (TYPEOF(result) == EXPRSXP) { /* parse returns an expr vector, you want the first */
		result = eval(VECTOR_ELT(result, 0), R_GlobalEnv);
		PrintValue(result);
	}

	return result;
}


