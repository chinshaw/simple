set(protobuf_source_files
	rexp.pb-c.c
	rexp.pb-c.h
)

add_library(rexp-pb SHARED ${protobuf_source_files})	
