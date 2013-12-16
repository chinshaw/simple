#include "ream.h"
#include <time.h>
#include <locale.h>
#include <langinfo.h>
#include <arpa/inet.h>
#include <google/protobuf/io/coded_stream.h>

using namespace google::protobuf::io;

#include <iostream>
#define PSIZE 4096
static uint8_t ERROR_MSG = 0x00;
static uint8_t PRINT_MSG = 0x01;
static uint8_t SET_STATUS = 0x02;
static uint8_t SET_COUNTER = 0x03;
uint32_t total_count;
using namespace std;
uint32_t BSIZE= 32768;
class OutputInfo{
public:
	REXP *rxp;
	string *rxp_s;
	void* inputbuffer;
	OutputInfo(void){
		rxp = new REXP();
		rxp_s = new string();
		inputbuffer = (void*)malloc(BSIZE);
	}

	~OutputInfo(){
		delete rxp;
		delete rxp_s;
		free(inputbuffer);
	}
};



OutputInfo oiinfo;// = new OutputInfo();


void Re_ShowMessage(const char* mess){
	Re_WriteConsoleEx(mess,strlen(mess),0);
}

void Re_WriteConsoleEx(const char *buf1, int len, int oType){
#ifndef FILEREADER
	switch(oType){
	case 0:
	  {
		fwrite(&PRINT_MSG,sizeof(uint8_t),1,CMMNC->BSTDERR);
		int len_rev =  reverseUInt(len);
		fwrite(&len_rev,sizeof(uint32_t),1,CMMNC->BSTDERR);
		fwrite(buf1,len,1,CMMNC->BSTDERR);
		return;
	  }
	case 1:
	  {
	    const char* state =CHAR(STRING_ELT( Rf_findVar(Rf_install(".rhipe.current.state"), R_GlobalEnv),0));
	    char buffme[4096];
	    snprintf(buffme,4096,"\nR ERROR BEGIN (%s):\n=============\n\n%s\nR ERROR END\n===========\n",state,buf1);
	    int k = strlen(buffme);
	    fwrite(&ERROR_MSG,sizeof(uint8_t),1,CMMNC->BSTDERR);
	    int len_rev =  reverseUInt(k);
	    fwrite(&len_rev,sizeof(uint32_t),1,CMMNC->BSTDERR);
	    fwrite(buffme,k,1,CMMNC->BSTDERR);
	    return;
	  }
	}
#endif
	fwrite(buf1,len,1,CMMNC->BSTDERR);
}


void mcount(const char *g1, const char* g2, uint32_t n){
  SEXP g,s1,s2,s3;
  PROTECT(g = Rf_allocVector(VECSXP,3));

  PROTECT(s1 = Rf_allocVector(STRSXP,1));
  PROTECT(s2 = Rf_allocVector(STRSXP,1));
  PROTECT(s3 = Rf_allocVector(REALSXP,1));

  SET_STRING_ELT(s1,0,Rf_mkChar(g1));
  SET_VECTOR_ELT(g,0,s1);

  SET_STRING_ELT(s2,0,Rf_mkChar(g2));
  SET_VECTOR_ELT(g,1,s2);

  REAL(s3)[0] = (double)n;
  SET_VECTOR_ELT(g,2,s3);
  
  UNPROTECT(3);

  counter(g);
  UNPROTECT(1);

}
void merror(const char *fmt, ...)
{
	va_list args;
	char errmsg[512];
	va_start(args,fmt);
	vsnprintf(errmsg,sizeof(errmsg),fmt,args);
	va_end(args);
	Re_WriteConsoleEx(errmsg,strlen(errmsg),1);
}

void mmessage(char *fmt, ...)
{
	va_list args;
	char errmsg[512];
	va_start(args,fmt);
	vsnprintf(errmsg,sizeof(errmsg),fmt,args);
	va_end(args);
	Re_WriteConsoleEx(errmsg,strlen(errmsg),0);
}

SEXP counter(SEXP listmoo){
	REXP *rxp = new REXP();
	SEXP result;
	rxp->Clear();
	sexpToRexp(rxp,listmoo);
	int size = rxp->ByteSize();
	PROTECT(result = Rf_allocVector(RAWSXP,size));
	if(result != R_NilValue){
		fwrite(&SET_COUNTER,sizeof(uint8_t),1,CMMNC->BSTDERR);
		writeVInt64ToFileDescriptor( size , CMMNC->BSTDERR);
		rxp->SerializeWithCachedSizesToArray(RAW(result));
		fwrite(RAW(result), size,1,CMMNC->BSTDERR);
	}
	UNPROTECT(1);
	delete rxp;
	return(R_NilValue);
}

SEXP status(SEXP mess){
	if(TYPEOF(mess)!=STRSXP){
		Rf_error("Must give a string");
		return(R_NilValue);
	}
	char *status = (char*)CHAR(STRING_ELT( mess , 0));
	fwrite(&SET_STATUS,sizeof(uint8_t),1,CMMNC->BSTDERR);
	uint32_t stle = strlen(status);
	uint32_t len_rev =  reverseUInt(stle);
	fwrite(&len_rev,sizeof(uint32_t),1,CMMNC->BSTDERR);
	fwrite(status,stle,1,CMMNC->BSTDERR);
	return(R_NilValue);

}


static inline uint32_t tobytes(SEXP x,std::string* result){
	REXP r = REXP();
	sexpToRexp(&r,x);
	uint32_t size = r.ByteSize();
	r.SerializeToString(result);
	return(size);
}


SEXP readFromMem(void * array,uint32_t nbytes){
	SEXP r = R_NilValue;
	oiinfo.rxp->Clear();
	if (nbytes > BSIZE)
	{
		oiinfo.inputbuffer=realloc(oiinfo.inputbuffer,nbytes+1024);
		if (!oiinfo.inputbuffer){
			merror("Memory Exhausted, could not realloc buffer in readFromHadoop\n");
			return(R_NilValue);
		}
		BSIZE=nbytes+1024;
	}
	if (oiinfo.rxp->ParseFromArray(array,nbytes)){
		PROTECT(r = rexpToSexp(*(oiinfo.rxp)));
		UNPROTECT(1);
	}
	return(r);
}

int32_t readJavaInt(FILE* fp){
	int32_t t = 0,rt=0;
	fread(&t,sizeof(int32_t),1,fp);
	rt = reverseUInt(t); //something not good here, rt is int32_t
	return(rt);
}
    
SEXP persUnser(SEXP robj)
{
	SEXP ans  = R_NilValue;
	REXP *rexp_container = new REXP();
	CodedInputStream cds(RAW(robj),LENGTH(robj));
	cds.SetTotalBytesLimit(256*1024*1024,256*1024*1024);
	if(rexp_container->ParseFromCodedStream(&cds)){
	  PROTECT(ans = rexpToSexp(*rexp_container));
	  UNPROTECT(1);
	}
	delete(rexp_container);
	return(ans);
}

SEXP persSer(SEXP robj){
  REXP *rexp_container = new REXP();
  rexp_container->Clear();
  sexpToRexp(rexp_container, robj);
  int bs = rexp_container->ByteSize();
  SEXP result = R_NilValue;
  PROTECT(result = Rf_allocVector(RAWSXP,bs));
  rexp_container->SerializeWithCachedSizesToArray(RAW(result));
  UNPROTECT(1);
  delete (rexp_container);
  return (result);
}


SEXP dbgstr(SEXP robj)
{
	SEXP ans  = R_NilValue;
	REXP *rexp_container = new REXP();
	rexp_container->ParseFromArray(RAW(robj),LENGTH(robj));
	PROTECT(ans = Rf_allocVector(STRSXP,1));
	SET_STRING_ELT(ans,0,Rf_mkChar(rexp_container->DebugString().c_str()));    
	delete(rexp_container);
	UNPROTECT(1);
	return(ans);
}
