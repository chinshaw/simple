set(server_source_files
	rprotoserve.c
	radapter.c
	message.c
)

include_directories(/usr/share/R/include /Library/Frameworks/R.framework/Versions/3.0/Resources/include ${PROJECT_SOURCE_DIR}/protobuf)

add_executable(rprotoserve ${server_source_files})
add_library(server SHARED ${server_source_files})

target_link_libraries(rprotoserve  rexp-pb protobuf-c R)
