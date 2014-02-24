#include "com_simple_radapter_NativeAdapter.h"

#include <R.h>
#include <Rinternals.h>
#include <Rembedded.h>
#include <Rversion.h>
#include <R_ext/Parse.h>
#include <jni.h>

#include "Rinit.h"
#include "RadapterJni.h"
#include "Rcallbacks.h"
#include "protoutils.h"

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
cetype_t string_encoding = CE_NATIVE; /* default is native */
#define mkRChar(X) mkCharCE((X), string_encoding)
#endif

// Default arguments if not specified.
static char *default_args[] = { "--gui=none", "--silent" };

struct safeAssign_s {
	SEXP sym, val, rho;
};

static void safeAssign(void *data) {
	struct safeAssign_s *s = (struct safeAssign_s*) data;
	defineVar(s->sym, s->val, s->rho);
}

/**
 * This is used to initialize the r environment
 */
JNIEXPORT jint JNICALL Java_com_simple_radapter_NativeAdapter_initR(JNIEnv *env,
		jobject this, jobjectArray a) {
	int initRes;
	char *fallbackArgv[] = { "Rengine", 0 };
	char **argv = fallbackArgv;
	int argc = 1;

	engineObj = (*env)->NewGlobalRef(env, this);
	engineClass = (*env)->NewGlobalRef(env,
			(*env)->GetObjectClass(env, engineObj));
	eenv = env;

	if (a) { /* retrieve the content of the String[] and construct argv accordingly */
		int len = (int) (*env)->GetArrayLength(env, a);
		if (len > 0) {
			int i = 0;
			argv = (char**) malloc(sizeof(char*) * (len + 2));
			argv[0] = fallbackArgv[0];
			while (i < len) {
				jobject o = (*env)->GetObjectArrayElement(env, a, i);
				i++;
				if (o) {
					const char *c;
					c = (*env)->GetStringUTFChars(env, o, 0);
					if (!c)
						argv[i] = "";
					else {
						argv[i] = strdup(c);
						(*env)->ReleaseStringUTFChars(env, o, c);
					}
				} else
					argv[i] = "";
			}
			argc = len + 1;
			argv[argc] = 0;
		}
	}

	if (argc == 2 && !strcmp(argv[1], "--zero-init")) {/* special case for direct embedding (exp!) */
		initRinside();
		return 0;
	}

	initRes = initR(argc, argv);
	/* we don't release the argv in case R still needs it later (even if it shouldn't), but it's not really a significant leak */

	return initRes;
}

JNIEXPORT void JNICALL Java_com_simple_radapter_NativeAdapter_endR(JNIEnv *env, jobject this, jint exitCode) {
	Rf_endEmbeddedR(exitCode);
}

JNIEXPORT jbyteArray JNICALL Java_com_simple_radapter_NativeAdapter_evalScript(
		JNIEnv *env, jobject this, jstring jStringCommand) {

	const char *command = (*env)->GetStringUTFChars(env, jStringCommand, 0);

	SEXP sexp = rexpress(command);

	REXP rexp = REXP__INIT;
	sexpToRexp(&rexp, sexp);

	size_t packedSize = rexp__get_packed_size(&rexp);
	void *packed = malloc(packedSize);
	rexp__pack(&rexp, packed);

	jbyteArray result = (*env)->NewByteArray(env, packedSize);
	(*env)->SetByteArrayRegion(env, result, 0, packedSize, packed);

	//PROTOBUF_C_BUFFER_SIMPLE_CLEAR (message);

	(*env)->ReleaseStringUTFChars(env, jStringCommand, command);
	return result;
}

JNIEXPORT jlong JNICALL Java_com_simple_radapter_NativeAdapter_jniSetString(
		JNIEnv *env, jobject this, jstring s) {
	return SEXP2L(getString(env, s));
}

JNIEXPORT jboolean JNICALL Java_com_simple_radapter_NativeAdapter_jniAssign(
		JNIEnv *env, jobject this, jstring symName, jlong valL, jlong rhoL) {
	struct safeAssign_s s;

	s.sym = installString(env, symName);
	if (!s.sym || s.sym == R_NilValue)
		return JNI_FALSE;

	s.rho = rhoL ? L2SEXP(rhoL) : R_GlobalEnv;
	s.val = valL ? L2SEXP(valL) : R_NilValue;

	/* we have to use R_ToplevelExec because defineVar may fail on locked bindings */
	return R_ToplevelExec(safeAssign, (void*) &s) ? JNI_TRUE : JNI_FALSE;
}

