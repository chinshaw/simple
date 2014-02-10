#ifndef RADAPTER_H
#define RADAPTER_H

#include <R.h>
#include <Rinternals.h>

#define SEXP2L(s) ((jlong)(s))
#ifdef WIN64
#define L2SEXP(s) ((SEXP)((jlong)(s)))
#else
#define L2SEXP(s) ((SEXP)((jlong)((unsigned long)(s))))
#endif



const char *parseExceptionClassName = "com/simple/radapter/api/ParseException";


SEXP getString(JNIEnv *env, jstring s);


jint throwParseException(JNIEnv *env, char *message);

#endif

