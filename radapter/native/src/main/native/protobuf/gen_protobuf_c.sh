#!/bin/sh

# This utils requires the native version of the
# protobuf-c parserver. 
# http://code.google.com/p/protobuf-c/

protoc-c --c_out=. rexp.proto
