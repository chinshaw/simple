#include "server.h"
#include "rexp.pb-c.h"


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

// call (void)Rf_PrintValue(robj) in gdb


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

void sexpToRexp(REXP *rexp,const SEXP robj){
	fill_rexp(rexp,robj);
}


/**
 * Copy the properties from the sexp object to
 * the rexp object
 */
void fill_rexp(REXP *rexp, const SEXP robj){

	SEXP xx = ATTRIB(robj);
	int i;
	if (xx!=R_NilValue)
	{
		SEXP s = ATTRIB(robj);
		for (; s != R_NilValue; s = CDR(s))
		{
			Rf_PrintValue(s);
			rexp->attrname = (CHAR(PRINTNAME(TAG(s))));
			REXP attrvalue = REXP__INIT;
			rexp->attrvalue = &attrvalue;
			fill_rexp(rexp->attrvalue,
					CAR(s));
		}
	}
	fprintf(stderr, "Type of robj is %d\n", TYPEOF(robj));
	switch(TYPEOF(robj)){
		case LGLSXP:
		rexp->rclass = REXP__RCLASS__LOGICAL;
		for (i = 0; i< LENGTH(robj); i++)
		{
			int d = LOGICAL(robj)[i];
			switch(d){
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
		case INTSXP:
		rexp->rclass = REXP__RCLASS__INTEGER;
		for (i=0; i<LENGTH(robj); i++)
			rexp->intvalue = &(INTEGER(robj)[i]);
		break;
		case REALSXP:
		rexp->rclass = REXP__RCLASS__REAL;
		for (i=0; i<LENGTH(robj); i++) {
			rexp->realvalue = &(REAL(robj)[i]);
		}
		break;
		case RAWSXP:{
				    rexp->rclass = REXP__RCLASS__RAW;
				    int l = LENGTH(robj);
				    ProtobufCBinaryData binData;

				    binData.data = (RAW(robj),l); 
				    binData.len = l;

				    rexp->rawvalue = binData;

				    //rexp->rawvalue = (RAW(robj),l);
				    break;
			    }
		case CPLXSXP:{
				     rexp->rclass = REXP__RCLASS__COMPLEX;
				     for (i = 0; i<LENGTH(robj); i++)
				     {
					     CMPLX mp = CMPLX__INIT;
					     mp.real = COMPLEX(robj)[i].r;
					     mp.imag = COMPLEX(robj)[i].i;
				     }
				     break;
			     }
		case NILSXP:{
				    rexp->rclass = REXP__RCLASS__NULLTYPE;
				    break;
			    }
		case STRSXP:{
				    rexp->rclass = REXP__RCLASS__STRING;
				    for (i=0; i<LENGTH(robj); i++){
					    STRING cm = STRING__INIT;
					    if (STRING_ELT(robj,i) == NA_STRING)
						    cm.isna = REXP__RBOOLEAN__NA;
					    else
						    cm.strval = CHAR(STRING_ELT(robj,i));
				    }
				    break;
			    }
		case VECSXP:{
				    rexp->rclass = REXP__RCLASS__LIST;
				    for (i = 0; i < LENGTH(robj); i++) {
					    REXP subval = REXP__INIT;
					    rexp->rexpvalue = &subval;
					    fill_rexp(rexp->rexpvalue,VECTOR_ELT(robj,i));
				    }	
				    break;
			    }
		default:
			    rexp->rclass = REXP__RCLASS__NULLTYPE;
			    break;
	}
}
