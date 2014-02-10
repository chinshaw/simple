/* Generated by the protocol buffer compiler.  DO NOT EDIT! */

/* Do not generate deprecated warnings for self */
#ifndef PROTOBUF_C_NO_DEPRECATED
#define PROTOBUF_C_NO_DEPRECATED
#endif

#include "rexp.pb-c.h"
void   rexp__init
                     (REXP         *message)
{
  static REXP init_value = REXP__INIT;
  *message = init_value;
}
size_t rexp__get_packed_size
                     (const REXP *message)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &rexp__descriptor);
  return protobuf_c_message_get_packed_size ((const ProtobufCMessage*)(message));
}
size_t rexp__pack
                     (const REXP *message,
                      uint8_t       *out)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &rexp__descriptor);
  return protobuf_c_message_pack ((const ProtobufCMessage*)message, out);
}
size_t rexp__pack_to_buffer
                     (const REXP *message,
                      ProtobufCBuffer *buffer)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &rexp__descriptor);
  return protobuf_c_message_pack_to_buffer ((const ProtobufCMessage*)message, buffer);
}
REXP *
       rexp__unpack
                     (ProtobufCAllocator  *allocator,
                      size_t               len,
                      const uint8_t       *data)
{
  return (REXP *)
     protobuf_c_message_unpack (&rexp__descriptor,
                                allocator, len, data);
}
void   rexp__free_unpacked
                     (REXP *message,
                      ProtobufCAllocator *allocator)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &rexp__descriptor);
  protobuf_c_message_free_unpacked ((ProtobufCMessage*)message, allocator);
}
void   string__init
                     (STRING         *message)
{
  static STRING init_value = STRING__INIT;
  *message = init_value;
}
size_t string__get_packed_size
                     (const STRING *message)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &string__descriptor);
  return protobuf_c_message_get_packed_size ((const ProtobufCMessage*)(message));
}
size_t string__pack
                     (const STRING *message,
                      uint8_t       *out)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &string__descriptor);
  return protobuf_c_message_pack ((const ProtobufCMessage*)message, out);
}
size_t string__pack_to_buffer
                     (const STRING *message,
                      ProtobufCBuffer *buffer)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &string__descriptor);
  return protobuf_c_message_pack_to_buffer ((const ProtobufCMessage*)message, buffer);
}
STRING *
       string__unpack
                     (ProtobufCAllocator  *allocator,
                      size_t               len,
                      const uint8_t       *data)
{
  return (STRING *)
     protobuf_c_message_unpack (&string__descriptor,
                                allocator, len, data);
}
void   string__free_unpacked
                     (STRING *message,
                      ProtobufCAllocator *allocator)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &string__descriptor);
  protobuf_c_message_free_unpacked ((ProtobufCMessage*)message, allocator);
}
void   cmplx__init
                     (CMPLX         *message)
{
  static CMPLX init_value = CMPLX__INIT;
  *message = init_value;
}
size_t cmplx__get_packed_size
                     (const CMPLX *message)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &cmplx__descriptor);
  return protobuf_c_message_get_packed_size ((const ProtobufCMessage*)(message));
}
size_t cmplx__pack
                     (const CMPLX *message,
                      uint8_t       *out)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &cmplx__descriptor);
  return protobuf_c_message_pack ((const ProtobufCMessage*)message, out);
}
size_t cmplx__pack_to_buffer
                     (const CMPLX *message,
                      ProtobufCBuffer *buffer)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &cmplx__descriptor);
  return protobuf_c_message_pack_to_buffer ((const ProtobufCMessage*)message, buffer);
}
CMPLX *
       cmplx__unpack
                     (ProtobufCAllocator  *allocator,
                      size_t               len,
                      const uint8_t       *data)
{
  return (CMPLX *)
     protobuf_c_message_unpack (&cmplx__descriptor,
                                allocator, len, data);
}
void   cmplx__free_unpacked
                     (CMPLX *message,
                      ProtobufCAllocator *allocator)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &cmplx__descriptor);
  protobuf_c_message_free_unpacked ((ProtobufCMessage*)message, allocator);
}
void   script__init
                     (Script         *message)
{
  static Script init_value = SCRIPT__INIT;
  *message = init_value;
}
size_t script__get_packed_size
                     (const Script *message)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &script__descriptor);
  return protobuf_c_message_get_packed_size ((const ProtobufCMessage*)(message));
}
size_t script__pack
                     (const Script *message,
                      uint8_t       *out)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &script__descriptor);
  return protobuf_c_message_pack ((const ProtobufCMessage*)message, out);
}
size_t script__pack_to_buffer
                     (const Script *message,
                      ProtobufCBuffer *buffer)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &script__descriptor);
  return protobuf_c_message_pack_to_buffer ((const ProtobufCMessage*)message, buffer);
}
Script *
       script__unpack
                     (ProtobufCAllocator  *allocator,
                      size_t               len,
                      const uint8_t       *data)
{
  return (Script *)
     protobuf_c_message_unpack (&script__descriptor,
                                allocator, len, data);
}
void   script__free_unpacked
                     (Script *message,
                      ProtobufCAllocator *allocator)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &script__descriptor);
  protobuf_c_message_free_unpacked ((ProtobufCMessage*)message, allocator);
}
void   eval_response__init
                     (EvalResponse         *message)
{
  static EvalResponse init_value = EVAL_RESPONSE__INIT;
  *message = init_value;
}
size_t eval_response__get_packed_size
                     (const EvalResponse *message)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &eval_response__descriptor);
  return protobuf_c_message_get_packed_size ((const ProtobufCMessage*)(message));
}
size_t eval_response__pack
                     (const EvalResponse *message,
                      uint8_t       *out)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &eval_response__descriptor);
  return protobuf_c_message_pack ((const ProtobufCMessage*)message, out);
}
size_t eval_response__pack_to_buffer
                     (const EvalResponse *message,
                      ProtobufCBuffer *buffer)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &eval_response__descriptor);
  return protobuf_c_message_pack_to_buffer ((const ProtobufCMessage*)message, buffer);
}
EvalResponse *
       eval_response__unpack
                     (ProtobufCAllocator  *allocator,
                      size_t               len,
                      const uint8_t       *data)
{
  return (EvalResponse *)
     protobuf_c_message_unpack (&eval_response__descriptor,
                                allocator, len, data);
}
void   eval_response__free_unpacked
                     (EvalResponse *message,
                      ProtobufCAllocator *allocator)
{
  PROTOBUF_C_ASSERT (message->base.descriptor == &eval_response__descriptor);
  protobuf_c_message_free_unpacked ((ProtobufCMessage*)message, allocator);
}
const ProtobufCEnumValue rexp__rclass__enum_values_by_number[11] =
{
  { "STRING", "REXP__RCLASS__STRING", 0 },
  { "RAW", "REXP__RCLASS__RAW", 1 },
  { "REAL", "REXP__RCLASS__REAL", 2 },
  { "COMPLEX", "REXP__RCLASS__COMPLEX", 3 },
  { "INTEGER", "REXP__RCLASS__INTEGER", 4 },
  { "LIST", "REXP__RCLASS__LIST", 5 },
  { "LOGICAL", "REXP__RCLASS__LOGICAL", 6 },
  { "NULLTYPE", "REXP__RCLASS__NULLTYPE", 7 },
  { "REAL1", "REXP__RCLASS__REAL1", 8 },
  { "STRING1", "REXP__RCLASS__STRING1", 9 },
  { "INTEGER1", "REXP__RCLASS__INTEGER1", 10 },
};
static const ProtobufCIntRange rexp__rclass__value_ranges[] = {
{0, 0},{0, 11}
};
const ProtobufCEnumValueIndex rexp__rclass__enum_values_by_name[11] =
{
  { "COMPLEX", 3 },
  { "INTEGER", 4 },
  { "INTEGER1", 10 },
  { "LIST", 5 },
  { "LOGICAL", 6 },
  { "NULLTYPE", 7 },
  { "RAW", 1 },
  { "REAL", 2 },
  { "REAL1", 8 },
  { "STRING", 0 },
  { "STRING1", 9 },
};
const ProtobufCEnumDescriptor rexp__rclass__descriptor =
{
  PROTOBUF_C_ENUM_DESCRIPTOR_MAGIC,
  "REXP.RClass",
  "RClass",
  "REXP__RClass",
  "",
  11,
  rexp__rclass__enum_values_by_number,
  11,
  rexp__rclass__enum_values_by_name,
  1,
  rexp__rclass__value_ranges,
  NULL,NULL,NULL,NULL   /* reserved[1234] */
};
const ProtobufCEnumValue rexp__rboolean__enum_values_by_number[3] =
{
  { "F", "REXP__RBOOLEAN__F", 0 },
  { "T", "REXP__RBOOLEAN__T", 1 },
  { "NA", "REXP__RBOOLEAN__NA", 2 },
};
static const ProtobufCIntRange rexp__rboolean__value_ranges[] = {
{0, 0},{0, 3}
};
const ProtobufCEnumValueIndex rexp__rboolean__enum_values_by_name[3] =
{
  { "F", 0 },
  { "NA", 2 },
  { "T", 1 },
};
const ProtobufCEnumDescriptor rexp__rboolean__descriptor =
{
  PROTOBUF_C_ENUM_DESCRIPTOR_MAGIC,
  "REXP.RBOOLEAN",
  "RBOOLEAN",
  "REXP__RBOOLEAN",
  "",
  3,
  rexp__rboolean__enum_values_by_number,
  3,
  rexp__rboolean__enum_values_by_name,
  1,
  rexp__rboolean__value_ranges,
  NULL,NULL,NULL,NULL   /* reserved[1234] */
};
static const ProtobufCFieldDescriptor rexp__field_descriptors[13] =
{
  {
    "rclass",
    1,
    PROTOBUF_C_LABEL_REQUIRED,
    PROTOBUF_C_TYPE_ENUM,
    0,   /* quantifier_offset */
    PROTOBUF_C_OFFSETOF(REXP, rclass),
    &rexp__rclass__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "realValue",
    2,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_DOUBLE,
    PROTOBUF_C_OFFSETOF(REXP, n_realvalue),
    PROTOBUF_C_OFFSETOF(REXP, realvalue),
    NULL,
    NULL,
    1,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "intValue",
    3,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_SINT32,
    PROTOBUF_C_OFFSETOF(REXP, n_intvalue),
    PROTOBUF_C_OFFSETOF(REXP, intvalue),
    NULL,
    NULL,
    1,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "booleanValue",
    4,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_ENUM,
    PROTOBUF_C_OFFSETOF(REXP, n_booleanvalue),
    PROTOBUF_C_OFFSETOF(REXP, booleanvalue),
    &rexp__rboolean__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "stringValue",
    5,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_MESSAGE,
    PROTOBUF_C_OFFSETOF(REXP, n_stringvalue),
    PROTOBUF_C_OFFSETOF(REXP, stringvalue),
    &string__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "rawValue",
    6,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_BYTES,
    PROTOBUF_C_OFFSETOF(REXP, has_rawvalue),
    PROTOBUF_C_OFFSETOF(REXP, rawvalue),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "complexValue",
    7,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_MESSAGE,
    PROTOBUF_C_OFFSETOF(REXP, n_complexvalue),
    PROTOBUF_C_OFFSETOF(REXP, complexvalue),
    &cmplx__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "rexpValue",
    8,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_MESSAGE,
    PROTOBUF_C_OFFSETOF(REXP, n_rexpvalue),
    PROTOBUF_C_OFFSETOF(REXP, rexpvalue),
    &rexp__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "attrName",
    11,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_STRING,
    PROTOBUF_C_OFFSETOF(REXP, n_attrname),
    PROTOBUF_C_OFFSETOF(REXP, attrname),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "attrValue",
    12,
    PROTOBUF_C_LABEL_REPEATED,
    PROTOBUF_C_TYPE_MESSAGE,
    PROTOBUF_C_OFFSETOF(REXP, n_attrvalue),
    PROTOBUF_C_OFFSETOF(REXP, attrvalue),
    &rexp__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "singleRealValue",
    13,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_DOUBLE,
    PROTOBUF_C_OFFSETOF(REXP, has_singlerealvalue),
    PROTOBUF_C_OFFSETOF(REXP, singlerealvalue),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "singleStringValue",
    15,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_MESSAGE,
    0,   /* quantifier_offset */
    PROTOBUF_C_OFFSETOF(REXP, singlestringvalue),
    &string__descriptor,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "singleIntegerValue",
    16,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_SINT32,
    PROTOBUF_C_OFFSETOF(REXP, has_singleintegervalue),
    PROTOBUF_C_OFFSETOF(REXP, singleintegervalue),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
};
static const unsigned rexp__field_indices_by_name[] = {
  8,   /* field[8] = attrName */
  9,   /* field[9] = attrValue */
  3,   /* field[3] = booleanValue */
  6,   /* field[6] = complexValue */
  2,   /* field[2] = intValue */
  5,   /* field[5] = rawValue */
  0,   /* field[0] = rclass */
  1,   /* field[1] = realValue */
  7,   /* field[7] = rexpValue */
  12,   /* field[12] = singleIntegerValue */
  10,   /* field[10] = singleRealValue */
  11,   /* field[11] = singleStringValue */
  4,   /* field[4] = stringValue */
};
static const ProtobufCIntRange rexp__number_ranges[3 + 1] =
{
  { 1, 0 },
  { 11, 8 },
  { 15, 11 },
  { 0, 13 }
};
const ProtobufCMessageDescriptor rexp__descriptor =
{
  PROTOBUF_C_MESSAGE_DESCRIPTOR_MAGIC,
  "REXP",
  "REXP",
  "REXP",
  "",
  sizeof(REXP),
  13,
  rexp__field_descriptors,
  rexp__field_indices_by_name,
  3,  rexp__number_ranges,
  (ProtobufCMessageInit) rexp__init,
  NULL,NULL,NULL    /* reserved[123] */
};
static const protobuf_c_boolean string__is_na__default_value = 0;
static const ProtobufCFieldDescriptor string__field_descriptors[2] =
{
  {
    "strval",
    1,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_STRING,
    0,   /* quantifier_offset */
    PROTOBUF_C_OFFSETOF(STRING, strval),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "isNA",
    2,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_BOOL,
    PROTOBUF_C_OFFSETOF(STRING, has_isna),
    PROTOBUF_C_OFFSETOF(STRING, isna),
    NULL,
    &string__is_na__default_value,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
};
static const unsigned string__field_indices_by_name[] = {
  1,   /* field[1] = isNA */
  0,   /* field[0] = strval */
};
static const ProtobufCIntRange string__number_ranges[1 + 1] =
{
  { 1, 0 },
  { 0, 2 }
};
const ProtobufCMessageDescriptor string__descriptor =
{
  PROTOBUF_C_MESSAGE_DESCRIPTOR_MAGIC,
  "STRING",
  "STRING",
  "STRING",
  "",
  sizeof(STRING),
  2,
  string__field_descriptors,
  string__field_indices_by_name,
  1,  string__number_ranges,
  (ProtobufCMessageInit) string__init,
  NULL,NULL,NULL    /* reserved[123] */
};
static const double cmplx__real__default_value = 0;
static const ProtobufCFieldDescriptor cmplx__field_descriptors[2] =
{
  {
    "real",
    1,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_DOUBLE,
    PROTOBUF_C_OFFSETOF(CMPLX, has_real),
    PROTOBUF_C_OFFSETOF(CMPLX, real),
    NULL,
    &cmplx__real__default_value,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
  {
    "imag",
    2,
    PROTOBUF_C_LABEL_REQUIRED,
    PROTOBUF_C_TYPE_DOUBLE,
    0,   /* quantifier_offset */
    PROTOBUF_C_OFFSETOF(CMPLX, imag),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
};
static const unsigned cmplx__field_indices_by_name[] = {
  1,   /* field[1] = imag */
  0,   /* field[0] = real */
};
static const ProtobufCIntRange cmplx__number_ranges[1 + 1] =
{
  { 1, 0 },
  { 0, 2 }
};
const ProtobufCMessageDescriptor cmplx__descriptor =
{
  PROTOBUF_C_MESSAGE_DESCRIPTOR_MAGIC,
  "CMPLX",
  "CMPLX",
  "CMPLX",
  "",
  sizeof(CMPLX),
  2,
  cmplx__field_descriptors,
  cmplx__field_indices_by_name,
  1,  cmplx__number_ranges,
  (ProtobufCMessageInit) cmplx__init,
  NULL,NULL,NULL    /* reserved[123] */
};
static const ProtobufCFieldDescriptor script__field_descriptors[1] =
{
  {
    "code",
    1,
    PROTOBUF_C_LABEL_REQUIRED,
    PROTOBUF_C_TYPE_STRING,
    0,   /* quantifier_offset */
    PROTOBUF_C_OFFSETOF(Script, code),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
};
static const unsigned script__field_indices_by_name[] = {
  0,   /* field[0] = code */
};
static const ProtobufCIntRange script__number_ranges[1 + 1] =
{
  { 1, 0 },
  { 0, 1 }
};
const ProtobufCMessageDescriptor script__descriptor =
{
  PROTOBUF_C_MESSAGE_DESCRIPTOR_MAGIC,
  "Script",
  "Script",
  "Script",
  "",
  sizeof(Script),
  1,
  script__field_descriptors,
  script__field_indices_by_name,
  1,  script__number_ranges,
  (ProtobufCMessageInit) script__init,
  NULL,NULL,NULL    /* reserved[123] */
};
static const ProtobufCFieldDescriptor eval_response__field_descriptors[1] =
{
  {
    "exit_code",
    1,
    PROTOBUF_C_LABEL_OPTIONAL,
    PROTOBUF_C_TYPE_SINT32,
    PROTOBUF_C_OFFSETOF(EvalResponse, has_exit_code),
    PROTOBUF_C_OFFSETOF(EvalResponse, exit_code),
    NULL,
    NULL,
    0,            /* packed */
    0,NULL,NULL    /* reserved1,reserved2, etc */
  },
};
static const unsigned eval_response__field_indices_by_name[] = {
  0,   /* field[0] = exit_code */
};
static const ProtobufCIntRange eval_response__number_ranges[1 + 1] =
{
  { 1, 0 },
  { 0, 1 }
};
const ProtobufCMessageDescriptor eval_response__descriptor =
{
  PROTOBUF_C_MESSAGE_DESCRIPTOR_MAGIC,
  "EvalResponse",
  "EvalResponse",
  "EvalResponse",
  "",
  sizeof(EvalResponse),
  1,
  eval_response__field_descriptors,
  eval_response__field_indices_by_name,
  1,  eval_response__number_ranges,
  (ProtobufCMessageInit) eval_response__init,
  NULL,NULL,NULL    /* reserved[123] */
};
static const ProtobufCMethodDescriptor script_service__method_descriptors[1] =
{
  { "Eval", &script__descriptor, &eval_response__descriptor },
};
const unsigned script_service__method_indices_by_name[] = {
  0         /* Eval */
};
const ProtobufCServiceDescriptor script_service__descriptor =
{
  PROTOBUF_C_SERVICE_DESCRIPTOR_MAGIC,
  "ScriptService",
  "Eval",
  "ScriptService",
  "",
  1,
  script_service__method_descriptors,
  script_service__method_indices_by_name
};
void script_service__eval(ProtobufCService *service,
                          const Script *input,
                          EvalResponse_Closure closure,
                          void *closure_data)
{
  PROTOBUF_C_ASSERT (service->descriptor == &script_service__descriptor);
  service->invoke(service, 0, (const ProtobufCMessage *) input, (ProtobufCClosure) closure, closure_data);
}
void script_service__init (ScriptService_Service *service,
                           ScriptService_ServiceDestroy destroy)
{
  protobuf_c_service_generated_init (&service->base,
                                     &script_service__descriptor,
                                     (ProtobufCServiceDestroy) destroy);
}
