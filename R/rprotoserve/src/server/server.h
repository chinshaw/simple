#include <R.h>
#include <Rinternals.h>
#include <Rversion.h>
#include <Rembedded.h>
#include <Rdefines.h>
#include <R_ext/Parse.h>



#include "rexp.pb-c.h"


/*
 * MESSAGES.cc
 */
SEXP rexpress(const char*);
void sexpToRexp(REXP *, const SEXP);
void fill_rexp(REXP *, const SEXP );
SEXP rexpToSexp(const REXP);
void writeSexp32(FILE* fout, REXP* prexp_buffer, SEXP obj);
void writeSexp64(FILE* fout, REXP* prexp_buffer, SEXP obj);

