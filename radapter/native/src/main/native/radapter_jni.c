#include "com_simple_radapter_NativeAdapter.h"
#include "radapter.h"
#include <Rinternals.h>
#include <Rembedded.h>
#include <Rversion.h>
#include <R_ext/Parse.h>


#define R_INTERFACE_PTRS 1
#define CSTACK_DEFNS 1
#include <Rinterface.h>



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


const char *programName = "radapter";


JNIEXPORT jint JNICALL Java_com_simple_radapter_NativeAdapter_initR
(JNIEnv *env, jobject this, jobjectArray a)
{

      int initRes;
      char *fallbackArgv[]={"Rengine",0};
      char **argv=fallbackArgv;
      int argc=1;
      
#ifdef JRI_DEBUG
      printf("jniSetupR\n");
#endif
	  
      engineObj=(*env)->NewGlobalRef(env, this);
      engineClass=(*env)->NewGlobalRef(env, (*env)->GetObjectClass(env, engineObj));
      eenv=env;
      
      if (a) { /* retrieve the content of the String[] and construct argv accordingly */
          int len = (int)(*env)->GetArrayLength(env, a);
          if (len>0) {              
              int i=0;
              argv=(char**) malloc(sizeof(char*)*(len+2));
              argv[0]=fallbackArgv[0];
              while (i < len) {
                  jobject o=(*env)->GetObjectArrayElement(env, a, i);
                  i++;
                  if (o) {
                      const char *c;
                      c=(*env)->GetStringUTFChars(env, o, 0);
                      if (!c)
                          argv[i]="";
                      else {
			  argv[i] = strdup(c);
                          (*env)->ReleaseStringUTFChars(env, o, c);
                      }
                  } else
                      argv[i]="";
              }
              argc=len+1;
              argv[argc]=0;
          }
      }

      if (argc==2 && !strcmp(argv[1],"--zero-init")) {/* special case for direct embedding (exp!) */
	initRinside();
	return 0;
      }
      
      initRes=initR(argc, argv);
      /* we don't release the argv in case R still needs it later (even if it shouldn't), but it's not really a significant leak */
      
      return initRes;
}



JNIEXPORT void JNICALL Java_com_simple_radapter_NativeAdapter_endR(JNIEnv *env, jobject this, jint exitCode) {
	Rf_endEmbeddedR(exitCode);
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

jint throwParseException(JNIEnv *env, char *message) {

	jclass		 exClass;

	exClass = (*env)->FindClass( env, parseExceptionClassName);
	return (*env)->ThrowNew( env, exClass, message );
}
