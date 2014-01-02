set(client_source_files
	client.c
	cli.c
)


include_directories(/usr/share/R/include ${PROJECT_SOURCE_DIR}/protobuf)
add_library(client SHARED ${client_source_files})
add_executable(rprotoserve-client ${client_source_files})
target_link_libraries(rprotoserve-client rexp-pb protobuf-c)
