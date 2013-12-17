
#include <R.h>
#include <Rinternals.h>
#include <Rdefines.h>
#include <Rversion.h>
#include <R_ext/Parse.h>

/* the # of arguments to R_ParseVector changed since R 2.5.0 */
#if R_VERSION < R_Version(2,5,0)
#define RPROTOSERVE_ParseVector R_ParseVector
#else
#define RPROTOSERVE_ParseVector(A,B,C) R_ParseVector(A,B,C,R_NilValue)
#endif


SEXP parseString(const char *s, int *parts, ParseStatus *status) {
}
