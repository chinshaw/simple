#ifndef RADAPTER_JNI_H
#define RADAPTER_JNI_H

#include <R.h>
#include <Rinternals.h>
#include <Rembedded.h>
#include <Rversion.h>
#include <R_ext/Parse.h>

#include <jni.h>


#define SEXP2L(s) ((jlong)(s))
#ifdef WIN64
#define L2SEXP(s) ((SEXP)((jlong)(s)))
#else
#define L2SEXP(s) ((SEXP)((jlong)((unsigned long)(s))))
#endif


/**
 * Global definitions
 */
extern jobject engineObj;
extern jclass engineClass;
extern JNIEnv *eenv;



int initR(int argc, char *argv[]);

int stopR();


/**
 * Get a string from the environment by its var name
 */
SEXP getString(JNIEnv *env, jstring var);

/**
 * install a string in the R environment
 */
SEXP installString(JNIEnv *env, jstring s);

/**
 * Execute a a single command.
 */
SEXP rexpress(const char *cmd);



/**
 * Error handling from jni
 */
jint throwParseException(JNIEnv *env, char *message);



void jri_checkExceptions(JNIEnv *env, int describe);

#endif

