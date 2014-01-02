#include <ctype.h>
#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include "rexp.pb-c.h"
#include <google/protobuf-c/protobuf-c-rpc.h>

static void *xmalloc (size_t size)
{
	void *rv;
	if (size == 0)
		return NULL;
	rv = malloc (size);
	return rv;
}


/**
 * This is used to create a new protobufc service.
 */
ProtobufCService *client_connect(ProtobufC_RPC_AddressType address_type, const char *name)
{
	ProtobufCService *service;
	ProtobufC_RPC_Client *client;

	service = protobuf_c_rpc_client_new (address_type, name, &script_service__descriptor, NULL);
	if (service == NULL) {
		return -1;
	}
	client = (ProtobufC_RPC_Client *) service;

	fprintf (stderr, "Connecting... ");
	while (!protobuf_c_rpc_client_is_connected (client))
		protobuf_c_dispatch_run (protobuf_c_dispatch_default ());
	fprintf (stderr, "done.\n");
	return service;
}

/**
 * Execute a command on the specified service.
 */
REXP *exec_command(ProtobufCService *service, const char *command) {
	Script script = SCRIPT__INIT;
	script.code = command;
	protobuf_c_boolean is_done = 0;
	
	int exit_code;

	void command_callback(const EvalResponse *result, void *closure_data) {
		exit_code = result->exit_code;
		*(protobuf_c_boolean *) closure_data = 1;
	}

	script_service__eval (service, &script, command_callback, &is_done);
	while (!is_done)
		protobuf_c_dispatch_run (protobuf_c_dispatch_default ());

	
	fprintf(stderr, "Response was %d", exit_code);
}

static void
handle_query_response (const EvalResponse *result,
		void *closure_data)
{
	fprintf(stderr, "Response was %d", result->exit_code);

	*(protobuf_c_boolean *) closure_data = 1;
}

