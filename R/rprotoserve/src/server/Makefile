

PBINC=`pkg-config --cflags protobuf`
PBLIB=`pkg-config --libs protobuf`
RINC=`/usr/bin/R CMD config CFLAGS`
RLIB=`/usr/bin/R CMD config --ldflags`

#FOR RELEASE USE
CFLAGS=`/usr/bin/R CMD config CXXFLAGS`
#FOR DEBUG USE
#CXXFLAGS= -WALL -g


#LINKER FOR SHARED LIB
SHLIB_CXXLD=`${R_HOME}/bin/R CMD config SHLIB_CXXLD`
SHLIB_CXXLDFLAGS=`${R_HOME}/bin/R CMD config SHLIB_CXXLDFLAGS`

MYCXXFLAGS+= -I. -I/usr/share/R/include -I../common -L../common -lrexp -lprotobuf-c ${CFLAGS} -DUSEAUTOSHORT -DHAVE_UINTPTR_T  ${RINC} ${PBINC}
PKG_LIBS+=-fpic ${RLIB} ${PBLIB}


all: app

rprotoserve.o: rprotoserve.c
	$(CC) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@

radapter.o: radapter.c
	$(CC) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
	
app: rprotoserve.o radapter.o
	$(CC)  $^ -o rprotoserve ${MYCXXFLAGS} ${PKG_LIBS} ${RINC} ${RLIB}

clean:
	rm -fr *.o
