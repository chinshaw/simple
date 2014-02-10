#ifndef RADAPTER_JNI_H
#define RADAPTER_JNI_H

#include <R.h>
#include <Rinternals.h>
#include <jni.h>

#define SEXP2L(s) ((jlong)(s))
#ifdef WIN64
#define L2SEXP(s) ((SEXP)((jlong)(s)))
#else
#define L2SEXP(s) ((SEXP)((jlong)((unsigned long)(s))))
#endif



extern jobject engineObj;
extern jclass engineClass;
extern JNIEnv *eenv;

const char *parseExceptionClassName = "com/simple/radapter/api/ParseException";

SEXP getString(JNIEnv *env, jstring s);


jint throwParseException(JNIEnv *env, char *message);

#endif

