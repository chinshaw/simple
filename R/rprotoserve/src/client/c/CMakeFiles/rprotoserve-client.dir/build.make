# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 2.8

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list

# Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/local/bin/cmake

# The command to remove a file.
RM = /usr/local/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The program to use to edit the cache.
CMAKE_EDIT_COMMAND = /usr/local/bin/ccmake

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src

# Include any dependencies generated for this target.
include client/c/CMakeFiles/rprotoserve-client.dir/depend.make

# Include the progress variables for this target.
include client/c/CMakeFiles/rprotoserve-client.dir/progress.make

# Include the compile flags for this target's objects.
include client/c/CMakeFiles/rprotoserve-client.dir/flags.make

client/c/CMakeFiles/rprotoserve-client.dir/client.c.o: client/c/CMakeFiles/rprotoserve-client.dir/flags.make
client/c/CMakeFiles/rprotoserve-client.dir/client.c.o: client/c/client.c
	$(CMAKE_COMMAND) -E cmake_progress_report /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/CMakeFiles $(CMAKE_PROGRESS_1)
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Building C object client/c/CMakeFiles/rprotoserve-client.dir/client.c.o"
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -o CMakeFiles/rprotoserve-client.dir/client.c.o   -c /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c/client.c

client/c/CMakeFiles/rprotoserve-client.dir/client.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/rprotoserve-client.dir/client.c.i"
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -E /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c/client.c > CMakeFiles/rprotoserve-client.dir/client.c.i

client/c/CMakeFiles/rprotoserve-client.dir/client.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/rprotoserve-client.dir/client.c.s"
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c && /usr/bin/cc  $(C_DEFINES) $(C_FLAGS) -S /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c/client.c -o CMakeFiles/rprotoserve-client.dir/client.c.s

client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.requires:
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.requires

client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.provides: client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.requires
	$(MAKE) -f client/c/CMakeFiles/rprotoserve-client.dir/build.make client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.provides.build
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.provides

client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.provides.build: client/c/CMakeFiles/rprotoserve-client.dir/client.c.o

# Object files for target rprotoserve-client
rprotoserve__client_OBJECTS = \
"CMakeFiles/rprotoserve-client.dir/client.c.o"

# External object files for target rprotoserve-client
rprotoserve__client_EXTERNAL_OBJECTS =

client/c/rprotoserve-client: client/c/CMakeFiles/rprotoserve-client.dir/client.c.o
client/c/rprotoserve-client: client/c/CMakeFiles/rprotoserve-client.dir/build.make
client/c/rprotoserve-client: protobuf/librexp-pb.dylib
client/c/rprotoserve-client: client/c/CMakeFiles/rprotoserve-client.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --red --bold "Linking C executable rprotoserve-client"
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/rprotoserve-client.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
client/c/CMakeFiles/rprotoserve-client.dir/build: client/c/rprotoserve-client
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/build

client/c/CMakeFiles/rprotoserve-client.dir/requires: client/c/CMakeFiles/rprotoserve-client.dir/client.c.o.requires
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/requires

client/c/CMakeFiles/rprotoserve-client.dir/clean:
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c && $(CMAKE_COMMAND) -P CMakeFiles/rprotoserve-client.dir/cmake_clean.cmake
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/clean

client/c/CMakeFiles/rprotoserve-client.dir/depend:
	cd /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c /Users/chinshaw/devel/workspace/simple/R/rprotoserve/src/client/c/CMakeFiles/rprotoserve-client.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : client/c/CMakeFiles/rprotoserve-client.dir/depend

