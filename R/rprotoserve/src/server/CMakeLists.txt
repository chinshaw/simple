set(server_source_files
	rprotoserve.c
	rprotoserve.h
)

add_executable(rprotoserve ${server_source_files})
target_link_libraries(rprotoserve rexp-pb protobuf-c)
