set(client_source_files
	client.c
	client.h
)

add_library(client ${client_source_files})

target_link_libraries(rexp-pb)
