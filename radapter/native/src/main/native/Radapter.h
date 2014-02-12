#ifndef RADAPTER_H
#define RADAPTER_H

#include <R.h>
#include <Rinternals.h>

#include "protobuf/protoutils.h"

/**
 * radapter.c
 */
REXP eval_script(const char *cmd);
SEXP rexpress(const char *cmd);
int initR(int argc, char *argv[]);
int stopR();


#endif

