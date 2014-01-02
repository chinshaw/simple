#include "server.h"


const char *server_addr = "127.0.0.1:9999";


/**
 * Test that a real is received.
 */
void testReal() {
	const char *command = "x <- 4";
	
	fprintf(stderr, "Doing connect\n");
	ProtobufCService *client = client_connect(PROTOBUF_C_RPC_ADDRESS_TCP, server_addr);
	fprintf(stderr, "Executing command\n");
	REXP *rexp = exec_command(client, command);
	//REXP rexp = eval_script(command);
	/*
	if ( rexp.rclass != REXP__RCLASS__REAL) {
		fprintf(stderr, "testReal FAILED: invalid rclass type %d\n", rexp.rclass);
	} 
	
	if (*rexp.realvalue != 4) {
		fprintf(stderr, "testReal FAILED: invalid value %f\n", *rexp.realvalue);
	}
	*/
}

/*

void testString() {
	const char *command = "x <- 'foo'";

	REXP rexp = eval_script(command);
	if ( rexp.rclass != REXP__RCLASS__STRING) {
		fprintf(stderr, "testString FAILED: invalid rclass type %d\n", rexp.rclass);
	} 
}


void testComplex() {
	const char *command = "n = c(2,3,5); s = c('aa', 'bb', 'cc'); b = c(TRUE, FALSE, TRUE); df = data.frame(n, s, b); ";

	REXP rexp = eval_script(command);
	if ( rexp.rclass != REXP__RCLASS__STRING) {
		fprintf(stderr, "testString FAILED: invalid rclass type %d\n", rexp.rclass);
	} 
}
*/

int main(int argc, char **argv) {
	fprintf(stderr, "Starting server\n");
	start_server(PROTOBUF_C_RPC_ADDRESS_TCP, server_addr);

	testReal();

//	testReal();
//	testString();
//	testComplex();
}
