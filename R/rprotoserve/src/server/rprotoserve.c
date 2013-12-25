#include <ctype.h>
#include <stdarg.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include "rexp.pb-c.h"
#include "server.h"
#include <google/protobuf-c/protobuf-c-rpc.h>

static unsigned database_size;

static void
die (const char *format, ...)
{
  va_list args;
  va_start (args, format);
  vfprintf (stderr, format, args);
  va_end (args);
  fprintf (stderr, "\n");
  exit (1);
}

static void
usage (void)
{
  die ("usage: example-server [--port=NUM | --unix=PATH] --database=INPUT\n"
       "\n"
       "Run a protobuf server as specified by the DirLookup service\n"
       "in the test.proto file in the protobuf-c distribution.\n"
       "\n"
       "Options:\n"
       "  --port=NUM       Port to listen on for RPC clients.\n"
       "  --unix=PATH      Unix-domain socket to listen on.\n"
       "  --database=FILE  data which the server will use to answer requests.\n"
       "\n"
       "The database file is a sequence of stanzas, one per person:\n"
       "\n"
       "dave\n"
       " email who@cares.com\n"
       " mobile (123)123-1234\n"
       " id 666\n"
       "\n"
       "notes:\n"
       "- each stanza begins with a single unindented line, the person's name.\n");
}

static void *xmalloc (size_t size)
{
  void *rv;
  if (size == 0)
    return NULL;
  rv = malloc (size);
  if (rv == NULL)
    die ("out-of-memory allocating %u bytes", (unsigned) size);
  return rv;
}

static void *xrealloc (void *a, size_t size)
{
  void *rv;
  if (size == 0)
    {
      free (a);
      return NULL;
    }
  if (a == NULL)
    return xmalloc (size);
  rv = realloc (a, size);
  if (rv == NULL)
    die ("out-of-memory re-allocating %u bytes", (unsigned) size);
  return rv;
}

static char *xstrdup (const char *str)
{
  if (str == NULL)
    return NULL;
  return strcpy (xmalloc (strlen (str) + 1), str);
}

static char *peek_next_token (char *buf)
{
  while (*buf && !isspace (*buf))
    buf++;
  while (*buf && isspace (*buf))
    buf++;
  return buf;
}

static protobuf_c_boolean is_whitespace (const char *text)
{
  while (*text != 0)
    {
      if (!isspace (*text))
        return 0;
      text++;
    }
  return 1;
}
static void chomp_trailing_whitespace (char *buf)
{
  unsigned len = strlen (buf);
  while (len > 0)
    {
      if (!isspace (buf[len-1]))
        break;
      len--;
    }
  buf[len] = 0;
}
static protobuf_c_boolean starts_with (const char *str, const char *prefix)
{
  return memcmp (str, prefix, strlen (prefix)) == 0;
}

static void script__eval (ScriptService_Service *service, 
			const Script *input, 
			EvalResponse_Closure closure, 
			void *closure_data) {
	fprintf(stderr, "Got request\n");

	initR(0, NULL);
	fprintf(stderr, "R is initialized\n");

	const char *script_code = xstrdup(input->code);
	fprintf(stderr, "Script code is %s\n", script_code);

	REXP sexp = eval_script(script_code);
	EvalResponse response = EVAL_RESPONSE__INIT;

	response.exit_code = 0;

	stopR();
	closure(&response, closure_data);
}

static ScriptService_Service script_service =
	SCRIPT_SERVICE__INIT(script__);

int main(int argc, char**argv)
{
  ProtobufC_RPC_Server *server;
  ProtobufC_RPC_AddressType address_type=0;
  const char *name = NULL;
  unsigned i;

  for (i = 1; i < (unsigned) argc; i++)
    {
      if (starts_with (argv[i], "--port="))
        {
          address_type = PROTOBUF_C_RPC_ADDRESS_TCP;
          name = strchr (argv[i], '=') + 1;
        }
      else if (starts_with (argv[i], "--unix="))
        {
          address_type = PROTOBUF_C_RPC_ADDRESS_LOCAL;
          name = strchr (argv[i], '=') + 1;
        }
      else
        usage ();
    }

  if (name == NULL)
    die ("missing --port=NUM or --unix=PATH");
  
  server = protobuf_c_rpc_server_new (address_type, name, (ProtobufCService *) &script_service, NULL);

  for (;;)
    protobuf_c_dispatch_run (protobuf_c_dispatch_default ());
  return 0;
}
