#include "protoutils.h"
#include "rexp.pb-c.h"
#include <stdlib.h>

/*
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
 */

int convertLogical[3] = { 0, 1, 2 };
// call (void)Rf_PrintValue(model) in gdb
SEXP rexpToSexp(const Radapter__Rexp *rexp) {

	SEXP s = R_NilValue;

	int i;
	switch (rexp->rclass) {
	case RADAPTER__REXP__RCLASS__NILSXP:
		return (R_NilValue);

	case RADAPTER__REXP__RCLASS__LGLSXP: {
		PROTECT(s = Rf_allocVector(LGLSXP, rexp->n_booleanvalue));
		for (i = 0; i < rexp->n_booleanvalue; i++) {
			Radapter__Rexp__RBOOLEAN v = rexp->booleanvalue[i];
			LOGICAL(s)[i] = convertLogical[1 * v];
		}
		break;
	}
	case RADAPTER__REXP__RCLASS__INTSXP: {
		PROTECT(s = Rf_allocVector(INTSXP, rexp->n_intvalue));
		for (i = 0; i < rexp->n_intvalue; i++) {
			INTEGER(s)[i] = rexp->intvalue[i];
		}
		break;
	}
	case RADAPTER__REXP__RCLASS__REALSXP: {
		PROTECT(s = Rf_allocVector(REALSXP, rexp->n_realvalue));
		for (i = 0; i < rexp->n_realvalue; i++)
			REAL(s)[i] = rexp->realvalue[i];
	}
	case RADAPTER__REXP__RCLASS__STRSXP: {
		PROTECT(s = Rf_allocVector(STRSXP, rexp->n_stringvalue));
		char *st;
		for (i = 0; i < rexp->n_stringvalue; i++) {
			// TODO handle NA

			st = rexp->stringvalue[i];
			SEXP y = Rf_mkChar(st);
			SET_STRING_ELT(s, i, y);
		}

	}
	}
	return (s);
}

/*
 SEXP rexpToSexp(const REXP *rexp){
 SEXP s = R_NilValue;
 int length;
 static int convertLogical[3]={0,1,NA_LOGICAL};
 switch(rexp->rclass()){


 case RADAPTER__REXP__RCLASS__RAW:
 {
 const string& r = rexp.rawvalue();
 length = r.size();
 PROTECT(s = Rf_allocVector(RAWSXP,length));
 memcpy(RAW(s),r.data(),length);
 break;
 }
 case RADAPTER__REXP__RCLASS__COMPLEX:
 length = rexp.complexvalue_size();
 PROTECT(s = Rf_allocVector(CPLXSXP,length));
 for (int i = 0; i<length; i++){
 COMPLEX(s)[i].r = rexp.complexvalue(i).real();
 COMPLEX(s)[i].i = rexp.complexvalue(i).imag();
 }
 break;
 case RADAPTER__REXP__RCLASS__STRING:
 {
 length = rexp.stringvalue_size();
 PROTECT(s = Rf_allocVector(STRSXP,length));
 STRING st;
 for (int i = 0; i<length; i++){
 st= rexp.stringvalue(i);
 if (st.isna())
 SET_STRING_ELT(s,i,R_NaString);
 else{
 SEXP y=  Rf_mkChar(st.strval().c_str());
 SET_STRING_ELT(s,i,y);
 }
 }
 break;
 }
 case RADAPTER__REXP__RCLASS__LIST:
 length = rexp.rexpvalue_size();
 PROTECT(s = Rf_allocVector(VECSXP,length));
 for (int i = 0; i< length; i++){
 // SEXP ik;
 SET_VECTOR_ELT(s, i, rexpToSexp(rexp.rexpvalue(i)) );
 }
 break;
 }
 int atlength = rexp.attrname_size();
 // int typ = TYPEOF(s);
 if (atlength>0  )
 {
 for (int j=0; j<atlength; j++)
 {
 // const char *nameofatt = rexp.attrname(j).c_str();
 // if(strcmp(nameofatt,"names")==0 && typ!=VECSXP) continue;
 // if(strcmp(nameofatt,"names")==0 && typ==VECSXP){
 //   SEXP v ;
 //   PROTECT(v= message2rexp(rexp.attrvalue(j)));
 //   if(!Rf_isNull(v)) Rf_setAttrib(s,Rf_install(nameofatt), v );
 //   UNPROTECT(1);
 // }
 //   	  SEXP n=Rf_mkString(nameofatt);
 // SEXP v ;
 // PROTECT(v= message2rexp(rexp.attrvalue(j)));
 // if(!Rf_isNull(v)) Rf_setAttrib(s,Rf_install(nameofatt), v );
 // UNPROTECT(1);

 // TEST TEST TEST TEST COULD FAILS
 // REVERT TO PREVIOUS CODE
 Rf_setAttrib(s,
 Rf_install(rexp.attrname(j).c_str()),
 rexpToSexp(rexp.attrvalue(j)));


 }
 }
 UNPROTECT(1);
 return(s); //Rf_duplicate(s)); //iff not forthis things crash, dont know why.
 }
 */

void sexpToRexp(Radapter__Rexp *rexp, const SEXP model) {
	fill_rexp(rexp, model);
}

/**
 * Copy the properties from the sexp object to
 * the rexp object
 */
void fill_rexp(Radapter__Rexp* rexp, const SEXP model) {
	fprintf(stderr, "Type of model is %d\n", TYPEOF(model));

	SEXP xx = ATTRIB(model);

	int i;
	if (xx != R_NilValue) {
		SEXP s = ATTRIB(model);
		//fprintf(stderr, "SIZE OF ATTRIBUTES %d", LENGTH(s));

		for (i = 0; s != R_NilValue; i++, s = CDR(s)) {
			//Rf_PrintValue(s);

			//REXP *attribute = malloc(sizeof(REXP));
			//*attribute = REXP_INIT;
			//fill_rexp(attribute, CAR(s));

			//rexp->attrname[i] = (CHAR(PRINTNAME(TAG(s))));
		}
	}

	switch (TYPEOF(model)) {
	case LGLSXP: //# define LGLSXP      10    /* logical vectors */
		rexp->rclass = RADAPTER__REXP__RCLASS__LGLSXP;
		for (i = 0; i < LENGTH(model); i++) {
			int d = LOGICAL(model)[i];
			switch (d) {
			Radapter__Rexp__RBOOLEAN value;
		case 0:
			value = RADAPTER__REXP__RBOOLEAN__F;
			break;
		case 1:
			value = RADAPTER__REXP__RBOOLEAN__T;
			break;
		default:
			value = RADAPTER__REXP__RBOOLEAN__NA;
			break;
			rexp->booleanvalue = &value;
			}
		}
		break;
	case INTSXP: // #define INTSXP      13    integer vectors
	{
		if (inherits(model, "factor")) {
			fill_rexp(rexp, Rf_asCharacterFactor(model));
		}

	}
		break;
	case REALSXP: //#define REALSXP     14    /* real variables */
		rexp->rclass = RADAPTER__REXP__RCLASS__REALSXP;
		rexp->n_realvalue = LENGTH(model);

		rexp->realvalue = malloc(sizeof(rexp->realvalue) * (rexp->n_realvalue));
		for (i = 0; i < rexp->n_realvalue; i++) {
			rexp->realvalue[i] = (REAL(model)[i]);
		}
		break;
	case RAWSXP: { //#define RAWSXP      24    /* raw bytes */
		rexp->rclass = RADAPTER__REXP__RCLASS__RAWSXP;
		rexp->has_rawvalue = 1;

		// set length of rexp;
		int rawLength = LENGTH(model);
		rexp->rawvalue.len = rawLength;

		// set the raw data for the rexp
		uint8_t *data = (uint8_t *) RAW(model);
		rexp->rawvalue.data = malloc(rawLength);

		memcpy(rexp->rawvalue.data, data, rawLength);

		break;
	}
	case CPLXSXP: { //#define CPLXSXP     15    /* complex variables */

		rexp->rclass = RADAPTER__REXP__RCLASS__CPLXSXP;
		for (i = 0; i < LENGTH(model); i++) {
			Radapter__CMPLX mp = RADAPTER__CMPLX__INIT;
			mp.real = COMPLEX(model)[i].r;
			mp.imag = COMPLEX(model)[i].i;
		}
		break;
	}
	case NILSXP: { //#define NILSXP       0    /* nil = NULL */
		rexp->rclass = RADAPTER__REXP__RCLASS__NILSXP;
		break;
	}
	case STRSXP: { // #define STRSXP      16    /* string vectors */
		rexp->rclass = RADAPTER__REXP__RCLASS__STRSXP;

		rexp->n_stringvalue = LENGTH(model);
		rexp->stringvalue = malloc(
				sizeof(rexp->stringvalue) * rexp->n_stringvalue);
		for (i = 0; i < rexp->n_stringvalue; i++) {
			rexp->stringvalue[i] = CHAR(STRING_ELT(model, i));
		}
		break;
	}

	case VECSXP: { // #define VECSXP      19    /* generic vectors */
		rexp->rclass = RADAPTER__REXP__RCLASS__VECSXP;
		rexp->n_rexpvalue = LENGTH(model);

		fprintf(stderr, "Size of vector is %zu\n", rexp->n_rexpvalue);
		rexp->rexpvalue = (Radapter__Rexp **) malloc(sizeof(Radapter__Rexp *) * rexp->n_rexpvalue);
		for (i = 0; i < rexp->n_rexpvalue; i++) {
			Radapter__Rexp subval = RADAPTER__REXP__INIT;

			Radapter__Rexp *sv = malloc(sizeof(Radapter__Rexp));
			*sv = subval;
			fill_rexp(sv, VECTOR_ELT(model, i));
			rexp->rexpvalue[i] = sv;
		}
		break;
	}
	default:
		rexp->rclass = RADAPTER__REXP__RCLASS__NILSXP;
		break;
	}
}
