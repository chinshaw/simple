#ifndef PROTO_UTILS_H
#define PROTO_UTILS_H


#include <R.h>
#include <Rinternals.h>
#include <Rversion.h>
#include <Rembedded.h>
#include <Rdefines.h>
#include <R_ext/Parse.h>

#include <protobuf-c-rpc/protobuf-c-rpc.h>

#include "rexp.pb-c.h"



/*  
 * Utility functions for converting to and fro protocol buffers
 * and SEXP objects
 */

void sexpToRexp(Radapter__Rexp *, const SEXP);

SEXP rexpToSexp(const Radapter__Rexp *rexp);

void fill_rexp(Radapter__Rexp *, const SEXP );
//void writeSexp32(FILE* fout, Radapter__Rexp* prexp_buffer, SEXP obj);
//void writeSexp64(FILE* fout, Radapter__Rexp* prexp_buffer, SEXP obj);




#endif

