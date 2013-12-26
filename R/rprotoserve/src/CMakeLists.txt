cmake_minimum_required (VERSION 2.6)
project (RPROTOSERVE)
# The version number.
set (RProtoServe_VERSION_MAJOR 1)
set (RProtoServe_VERSION_MINOR 0)
 
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g -L/usr/local/lib -L/Library/Frameworks/R.framework/Versions/3.0/Resources/lib/ -lR -lprotobuf-c")

# configure a header file to pass some of the CMake settings
# to the source code
configure_file (
  "${PROJECT_SOURCE_DIR}/rprotoserve_config.h.in"
  "${PROJECT_BINARY_DIR}/rprotoserve_config.h"
)
 
include_directories(${PROJECT_SOURCE_DIR}/protobuf)


add_subdirectory(protobuf)
add_subdirectory(server)
add_subdirectory(client/c)
add_subdirectory(test)

