#ifndef PROTO_UTILS_H
#define PROTO_UTILS_H


#include <R.h>
#include <Rinternals.h>
#include <Rversion.h>
#include <Rembedded.h>
#include <Rdefines.h>
#include <R_ext/Parse.h>

#include <google/protobuf-c/protobuf-c-rpc.h>

#include "rexp.pb-c.h"




/*  
 * Utility functions for converting to and fro protocol buffers
 * and SEXP objects
 */

void sexpToRexp(REXP *, const SEXP);

SEXP rexpToSexp(const REXP *rexp);

void fill_rexp(REXP *, const SEXP );
void writeSexp32(FILE* fout, REXP* prexp_buffer, SEXP obj);
void writeSexp64(FILE* fout, REXP* prexp_buffer, SEXP obj);




#endif

