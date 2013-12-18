cmake_minimum_required (VERSION 2.6)
project (RProtoServe)
# The version number.
set (RProtoServe_VERSION_MAJOR 1)
set (RProtoServe_VERSION_MINOR 0)
 
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -L/usr/local/lib -lprotobuf-c")

include_directories(${PROJECT_SOURCE_DIR}/protobuf)


add_subdirectory(protobuf)
add_subdirectory(server)
add_subdirectory(client/c)

# configure a header file to pass some of the CMake settings
# to the source code
configure_file (
  "${PROJECT_SOURCE_DIR}/rprotoserve_config.h.in"
  "${PROJECT_BINARY_DIR}/rprotoserve_config.h"
)
 
# add the binary tree to the search path for include files
# so that we will find rprotoserve_config.h
 
# add the executable
#add_executable(rprotoserve ${server_source_files})
#target_link_libraries(protobuf server protbuf-c)
