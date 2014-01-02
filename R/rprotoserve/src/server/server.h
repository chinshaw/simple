#include <R.h>
#include <Rinternals.h>
#include <Rversion.h>
#include <Rembedded.h>
#include <Rdefines.h>
#include <R_ext/Parse.h>

#include <google/protobuf-c/protobuf-c-rpc.h>


#include "rexp.pb-c.h"



/**
 * rprotoserve.c
 */
int start_server(ProtobufC_RPC_AddressType address_type, const char *name);

/**
 * radapter.c
 */
REXP eval_script(const char *cmd);
SEXP rexpress(const char *cmd);
int initR(int argc, char *argv[]);
int stopR();



/*
 * MESSAGES.cc
 */
void sexpToRexp(REXP *, const SEXP);
void fill_rexp(REXP *, const SEXP );
SEXP rexpToSexp(const REXP);
void writeSexp32(FILE* fout, REXP* prexp_buffer, SEXP obj);
void writeSexp64(FILE* fout, REXP* prexp_buffer, SEXP obj);

