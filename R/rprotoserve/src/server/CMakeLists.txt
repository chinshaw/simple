set(server_source_files
	rprotoserve.c
	rprotoserve.h
	radapter.c
	radapter.h
)

include_directories(/usr/share/R/include /Library/Frameworks/R.framework/Versions/3.0/Resources/include ${PROJECT_SOURCE_DIR}/protobuf)
add_executable(rprotoserve ${server_source_files})
target_link_libraries(rprotoserve  rexp-pb protobuf-c R)
