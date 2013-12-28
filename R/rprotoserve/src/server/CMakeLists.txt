set(server_source_files
	rprotoserve.c
	radapter.c
	message.c
)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g -L/usr/local/lib -lrexp-pb -lR -lprotobuf-c")

set(CMAKE_LIBRARY_PATH ${CMAKE_LIBRARY_PATH} /Library/Frameworks/R.framework/Libraries/lib)

include_directories(/usr/share/R/include /Library/Frameworks/R.framework/Versions/3.0/Resources/include ${PROJECT_SOURCE_DIR}/protobuf)

link_directories (${RPROTOSERVE_BINARY_DIR}/protobuf  /Library/Frameworks/R.framework/Versions/3.0/Resources/lib/) 

add_executable(rprotoserve ${server_source_files})
add_library(server SHARED ${server_source_files})

target_link_libraries(rprotoserve rexp-pb protobuf-c R)
