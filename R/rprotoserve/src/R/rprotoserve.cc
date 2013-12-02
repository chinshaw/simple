#include "rprotoserve.h"


using namespace std;


RProtoServe::RProtoServe() : port(5449) {

}

RProtoServe::RProtoServe(int port) : port(port) {

}


void start() {


	started = true;
}
