set(protobuf_source_files
	rexp.pb-c.c
	rexp.pb-c.h
)

add_custom_command (
	OUTPUT rexp.pb-c.h
	COMMAND protoc-c --c_out=. rexp.proto
	WORKING_DIRECTORY ${CMAKE_CURRENT_SOURCE_DIR}
)

add_custom_target(gen-protobuf rexp-pb DEPENDS rexp.pb-c.h)
add_library(rexp-pb SHARED ${protobuf_source_files})	
