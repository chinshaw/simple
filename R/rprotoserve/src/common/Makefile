



CFLAGS=`/usr/bin/R CMD config CXXFLAGS`
#FOR DEBUG USE
#CXXFLAGS= -WALL -g


#LINKER FOR SHARED LIB
SHLIB_CXXLD=`${R_HOME}/bin/R CMD config SHLIB_CXXLD`
SHLIB_CXXLDFLAGS=`${R_HOME}/bin/R CMD config SHLIB_CXXLDFLAGS`

MYCXXFLAGS+= -I. -I/usr/share/R/include -I../common/generated-code -lprotobuf-c ${CFLAGS} -DUSEAUTOSHORT -DHAVE_UINTPTR_T  ${RINC} ${PBINC}
PKG_LIBS+=-fpic ${RLIB} ${PBLIB}


all: gen-code librexp.so

gen-code:
	protoc-c --c_out=generated-code rexp.proto
	
librexp.so: generated-code/rexp.pb-c.c
	$(CC) -shared $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@

clean:
	rm -fr *.o




