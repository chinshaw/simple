set(server_source_files
	test_r_adapter.c
)

include_directories(/usr/share/R/include /Library/Frameworks/R.framework/Versions/3.0/Resources/include ${PROJECT_SOURCE_DIR}/server)

add_executable(test_r_adapter test_r_adapter.c)

target_link_libraries(test_r_adapter server protobuf-c rexp-pb R)
