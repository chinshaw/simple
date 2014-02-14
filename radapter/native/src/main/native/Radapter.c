#include "com_simple_radapter_NativeAdapter.h"
#include "globals.h"
#include <R.h>
#include <Rinternals.h>
#include <Rembedded.h>
#include <Rversion.h>
#include <R_ext/Parse.h>
#include <jni.h>


#include "protobuf/protoutils.h"

/* the # of arguments to R_ParseVector changed since R 2.5.0 */
#if R_VERSION < R_Version(2,5,0)
#define RPROTOSERVE_ParseVector R_ParseVector
#else
#define RPROTOSERVE_ParseVector(A,B,C) R_ParseVector(A,B,C,R_NilValue)
#endif


/* string encoding handling */
#if (R_VERSION < R_Version(2,8,0)) || (defined DISABLE_ENCODING)
#define mkRChar(X) mkChar(X)
#else
#define USE_ENCODING 1
cetype_t string_encoding = CE_NATIVE;  /* default is native */
#define mkRChar(X) mkCharCE((X), string_encoding)
#endif


// Default arguments if not specified.
char *default_args[] = {"--gui=none", "--silent"};


//  Whether or not R is initialized.
static int initialized = 0;

/**
 * This is used to initialize the r environment
 */
JNIEXPORT jint JNICALL Java_com_simple_radapter_NativeAdapter_initR
(JNIEnv *env, jobject this, jobjectArray a)
{

      int initRes;
      char *fallbackArgv[]={"Rengine",0};
      char **argv=fallbackArgv;
      int argc=1;
      
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

JNIEXPORT jbyteArray JNICALL eval_script(JNIEnv *env, jobject this, const char *command) 
{
	REXP rexp = REXP__INIT;
	if (! initialized) {
		fprintf(stderr, "R has not been initialized, need to call initR");
		return NULL;
	}	
	SEXP sexp = rexpress(command);
	sexpToRexp(&rexp, sexp);


	ProtobufCMessage *message = &rexp;
	size_t packedSize = protobuf_c_message_get_packed_size(message);
	void *packed = malloc (packedSize);
	protobuf_c_message_pack(message, packed);

	
	jbyteArray result = (*env)->NewByteArray(env, packedSize);
	(*env)->SetByteArrayRegion(env, jbyteArray, 0, packedSize, packed);
	
	PROTOBUF_C_BUFFER_SIMPLE_CLEAR (message);

	return result;
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

