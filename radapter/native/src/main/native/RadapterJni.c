#include "RadapterJni.h"


/* the # of arguments to R_ParseVector changed since R 2.5.0 */
#if R_VERSION < R_Version(2,5,0)
#define RS_ParseVector R_ParseVector
#else
#define RS_ParseVector(A,B,C) R_ParseVector(A,B,C,R_NilValue)
#endif


/* define mkCharUTF8 in a compatible fashion */
#if R_VERSION < R_Version(2,7,0)
#define mkCharUTF8(X) mkChar(X)
#define CHAR_UTF8(X) CHAR(X)
#else
#define mkCharUTF8(X) mkCharCE(X, CE_UTF8)
#define CHAR_UTF8(X) jri_char_utf8(X)
const char *jri_char_utf8(SEXP);
#endif

/**
 * Global definitions
 */
jobject engineObj;
jclass engineClass;
JNIEnv *eenv;

const char *programName = "radapter";

const char *parseExceptionClassName = "com/simple/radapter/api/ParseException";

void jri_error(char *fmt, ...) {
    va_list v;
    va_start(v,fmt);
    vprintf(fmt,v);
    va_end(v);
}



SEXP rexpress(const char* cmd)
{
	SEXP cmdSexp, cmdexpr, ans = R_NilValue;
	int i;
	ParseStatus status;
	PROTECT(cmdSexp = Rf_allocVector(STRSXP, 1));
	SET_STRING_ELT(cmdSexp, 0, Rf_mkChar(cmd));
	cmdexpr = PROTECT(R_ParseVector(cmdSexp, -1, &status, R_NilValue));
	if (status != PARSE_OK) {
		UNPROTECT(2);
		Rf_error("invalid call: %s", cmd);
		return(R_NilValue);
	}
	for(i = 0; i < Rf_length(cmdexpr); i++)
		ans = Rf_eval(VECTOR_ELT(cmdexpr, i), R_GlobalEnv);
	UNPROTECT(2);
	return(ans);
}



/*
 * @param rho long reflection of the environment where to evaluate
 *
 * @return 0 if an evaluation error ocurred or exp is 0
 */
JNIEXPORT jlong JNICALL Java_com_simple_radapter_NativeAdapter_jniEval
(JNIEnv *env, jobject this, jlong exp, jlong rho)
{
	SEXP es = R_NilValue, exps = L2SEXP(exp);
	SEXP eval_env = L2SEXP(rho);
	int errorOccurred = 0;
	int i = 0, l;

	/* invalid (NULL) expression (parse error, ... ) */
	if (!exp) return 0;

	if (TYPEOF(exps) == EXPRSXP) {
		/* if the object is a list of exps, eval them one by one */
		l = LENGTH(exps);
		while (i < l) {
			es = R_tryEval(VECTOR_ELT(exps,i), eval_env, &errorOccurred);

			/* an error occured, no need to continue */
			if (errorOccurred) {
				return 0;
			}
			i++;
		}
	} else {
		es = R_tryEval(exps, eval_env, &errorOccurred);
	}
	/* er is just a flag - on error return 0 */
	if (errorOccurred) {
		return 0;
	}

	return SEXP2L(es);
}


JNIEXPORT jlong JNICALL Java_com_simple_radapter_NativeAdapter_jniParse
(JNIEnv *env, jobject this, jstring str, jint parts)
{             
	ParseStatus ps; 
	SEXP pstr, cv;

	PROTECT(cv=getString(env, str));
	pstr=RS_ParseVector(cv, parts, &ps);
	UNPROTECT(1);
	return SEXP2L(pstr);
}



/** jobjRefInt object : string */
SEXP getString(JNIEnv *env, jstring s) {
    SEXP r;
    const char *c;

    if (!s) return ScalarString(R_NaString);
    c = (*env)->GetStringUTFChars(env, s, 0);
    if (!c) {
    	throwParseException(env, "getString : can't retrieve string content");
    }

    PROTECT(r = allocVector(STRSXP,1));
    SET_STRING_ELT(r, 0, mkCharUTF8(c));
    UNPROTECT(1);
    (*env)->ReleaseStringUTFChars(env, s, c);
    return r;
}

SEXP installString(JNIEnv *env, jstring s) {
    SEXP r;
    const char *c;

    if (!s) return R_NilValue;
    c=(*env)->GetStringUTFChars(env, s, 0);
    if (!c) {
        jri_error("jri_getString: can't retrieve string content");
        return R_NilValue;
    }
    r = install(c);
    (*env)->ReleaseStringUTFChars(env, s, c);
    return r;
}


jint throwParseException(JNIEnv *env, char *message) {

	jclass		 exClass;

	exClass = (*env)->FindClass( env, parseExceptionClassName);
	return (*env)->ThrowNew( env, exClass, message );
}


void jri_checkExceptions(JNIEnv *env, int describe)
{
    jthrowable t=(*env)->ExceptionOccurred(env);
    if (t) {
#ifndef JRI_DEBUG
        if (describe)
#endif
            (*env)->ExceptionDescribe(env);
        (*env)->ExceptionClear(env);
    }
}
