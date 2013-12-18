#include "radapter.h"

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



int initR(int argc, char *argv[]) {
	Rf_initEmbeddedR(argc, argv);
}

SEXP eval_script(const char *command) {
	SEXP e, result;
    	int errorOccurred;	
	Rf_initEmbeddedR(argc, argv);

	return result;
}
