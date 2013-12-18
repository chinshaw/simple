set(client_source_files
	client.c
	client.h
)


include_directories(/usr/share/R/include ${PROJECT_SOURCE_DIR}/protobuf)
add_executable(rprotoserve-client ${client_source_files})
target_link_libraries(rprotoserve-client rexp-pb protobuf-c)
