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

// call (void)Rf_PrintValue(model) in gdb

/*
 SEXP rexpToSexp(const REXP *rexp){
 SEXP s = R_NilValue;
 int length;
 static int convertLogical[3]={0,1,NA_LOGICAL};
 switch(rexp->rclass()){
 case REXP__RCLASS__NULLTYPE:
 return(R_NilValue);
 case REXP__RCLASS__LOGICAL:
 length = rexp.booleanvalue_size();
 PROTECT(s = Rf_allocVector(LGLSXP,length));
 for (int i = 0; i<length; i++)
 {
 REXP__RBOOLEAN v = rexp.booleanvalue(i);
 LOGICAL(s)[i] = convertLogical[1*v];
 }
 break;
 case REXP__RCLASS__INTEGER:
 length = rexp.intvalue_size();
 PROTECT(s = Rf_allocVector(INTSXP,length));
 for (int i = 0; i<length; i++)
 INTEGER(s)[i] = rexp.intvalue(i);
 break;
 case REXP__RCLASS__REAL
 length = rexp.realvalue_size();
 PROTECT(s = Rf_allocVector(REALSXP,length));
 for (int i = 0; i<length; i++)
 REAL(s)[i] = rexp.realvalue(i);
 break;
 case REXP__RCLASS__RAW:
 {
 const string& r = rexp.rawvalue();
 length = r.size();
 PROTECT(s = Rf_allocVector(RAWSXP,length));
 memcpy(RAW(s),r.data(),length);
 break;
 }
 case REXP__RCLASS__COMPLEX:
 length = rexp.complexvalue_size();
 PROTECT(s = Rf_allocVector(CPLXSXP,length));
 for (int i = 0; i<length; i++){
 COMPLEX(s)[i].r = rexp.complexvalue(i).real();
 COMPLEX(s)[i].i = rexp.complexvalue(i).imag();
 }
 break;
 case REXP__RCLASS__STRING:
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
 case REXP__RCLASS__LIST:
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

void sexpToRexp(REXP *rexp, const SEXP model) {
	fill_rexp(rexp, model);
}

/**
 * Copy the properties from the sexp object to
 * the rexp object
 */
void fill_rexp(REXP* rexp, const SEXP model) {
	fprintf(stderr, "Type of model is %d\n", TYPEOF(model));


	SEXP xx = ATTRIB(model);
	if (xx != R_NilValue) {
		SEXP s = ATTRIB(model);
		fprintf(stderr, "SIZE OF ATTRIBUTES %d", LENGTH(s));

		for (int i = 0; s != R_NilValue; i++, s = CDR(s)) {
			Rf_PrintValue(s);

			//REXP *attribute = malloc(sizeof(REXP));
			//*attribute = REXP_INIT;
			//fill_rexp(attribute, CAR(s));

			//rexp->attrname[i] = (CHAR(PRINTNAME(TAG(s))));


		}
	}

	int i;

	switch (TYPEOF(model)) {
	case LGLSXP: //# define LGLSXP      10    /* logical vectors */
		rexp->rclass = REXP__RCLASS__LGLSXP;
		for (i = 0; i < LENGTH(model); i++) {
			int d = LOGICAL(model)[i];
			switch (d) {
			REXP__RBOOLEAN value;
		case 0:
			value = REXP__RBOOLEAN__F;
			break;
		case 1:
			value = REXP__RBOOLEAN__T;
			break;
		default:
			value = REXP__RBOOLEAN__NA;
			break;
			rexp->booleanvalue = &value;
			}
		}
		break;
	case INTSXP: // #define INTSXP      13    /* integer vectors */
		// factors have internal type INTSXP too
		if (Rf_inherits(model, "factor")) {
			Rf_PrintValue(model);
			//Rf_PrintValue(CAR(model));
			int levelCount = Rf_nlevels(model);
			if (levelCount > 0) {
				fprintf(stderr, "Got a factor with count %d\n", levelCount);
				SEXP levels = GET_LEVELS(model);

				fill_rexp(rexp, levels);
			}
			break;
		}

		rexp->rclass = REXP__RCLASS__INTSXP;
		rexp->n_intvalue = LENGTH(model);
		rexp->intvalue = malloc(sizeof(rexp->intvalue) * (rexp->n_intvalue));
		for (i = 0; i < rexp->n_intvalue; i++) {
			fprintf(stderr, "Setting value of rexp to %d %d\n",i,  (INTEGER(model)[i]));
			rexp->intvalue[0] = (INTEGER(model)[i]);
		}

		break;
	case REALSXP: //#define REALSXP     14    /* real variables */
		rexp->rclass = REXP__RCLASS__REALSXP;
		rexp->n_realvalue = LENGTH(model);

		rexp->realvalue = malloc(sizeof(rexp->realvalue) * (rexp->n_realvalue));
		for (i = 0; i < rexp->n_realvalue; i++) {
			fprintf(stderr, "Setting value of rexp to %d %f\n",i,  (REAL(model)[i]));
			rexp->realvalue[i] = (REAL(model)[i]);
		}
		break;
	case RAWSXP: { //#define RAWSXP      24    /* raw bytes */
		rexp->rclass = REXP__RCLASS__RAWSXP;
		int l = LENGTH(model);
		ProtobufCBinaryData binData;

		//binData.data = (RAW(model), l);
		//binData.len = l;

		//rexp->rawvalue = binData;

		//rexp->rawvalue = (RAW(model),l);
		break;
	}
	case CPLXSXP: { //#define CPLXSXP     15    /* complex variables */
		rexp->rclass = REXP__RCLASS__CPLXSXP;
		for (i = 0; i < LENGTH(model); i++) {
			CMPLX mp = CMPLX__INIT;
			mp.real = COMPLEX(model)[i].r;
			mp.imag = COMPLEX(model)[i].i;
		}
		break;
	}
	case NILSXP: { //#define NILSXP       0    /* nil = NULL */
		rexp->rclass = REXP__RCLASS__NILSXP;
		break;
	}
	case STRSXP: { // #define STRSXP      16    /* string vectors */
		rexp->rclass = REXP__RCLASS__STRSXP;

		rexp->n_stringvalue = LENGTH(model);
		fprintf(stderr, "Number of strings %d\n", rexp->n_stringvalue);
		rexp->stringvalue = malloc(sizeof(rexp->stringvalue) * rexp->n_stringvalue);
		for (i = 0; i < rexp->n_stringvalue; i++) {
			rexp->stringvalue[i] = CHAR(STRING_ELT(model, i));
		}
		break;
	}

	case VECSXP: { // #define VECSXP      19    /* generic vectors */
		rexp->rclass = REXP__RCLASS__VECSXP;
		rexp->n_rexpvalue = LENGTH(model);

		fprintf(stderr, "Size of vector is %zu\n", rexp->n_rexpvalue);
		rexp->rexpvalue = (REXP **) malloc(sizeof(REXP *) * rexp->n_rexpvalue);
		for (i = 0; i < rexp->n_rexpvalue; i++) {
			REXP subval = REXP__INIT;

			REXP *sv = malloc(sizeof(REXP));
			*sv = subval;
			fill_rexp(sv, VECTOR_ELT(model, i));
			rexp->rexpvalue[i] = sv;
		}
		break;
	}
	default:
		rexp->rclass = REXP__RCLASS__NILSXP;
		break;
	}
}
