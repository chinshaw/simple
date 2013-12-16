

PBINC=`pkg-config --cflags protobuf`
PBLIB=`pkg-config --libs protobuf`
RINC=`/usr/bin/R CMD config --cppflags`
RLIB=`/usr/bin/R CMD config --ldflags`


#FOR RELEASE USE
CXXFLAGS=`/usr/bin/R CMD config CXXFLAGS`
#FOR DEBUG USE
#CXXFLAGS= -WALL -g


#LINKER FOR SHARED LIB
SHLIB_CXXLD=`${R_HOME}/bin/R CMD config SHLIB_CXXLD`
SHLIB_CXXLDFLAGS=`${R_HOME}/bin/R CMD config SHLIB_CXXLDFLAGS`

MYCXXFLAGS+= -I. -I/usr/share/R/include -lboost_system ${CXXFLAGS} -DUSEAUTOSHORT -DHAVE_UINTPTR_T  ${RINC} ${PBINC}
PKG_LIBS+=-fpic ${RLIB} ${PBLIB}


all: app

rexp.pb.o: rexp.pb.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
message.o: message.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
main.o: main.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
display.o: display.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
fileio.o: fileio.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
signal.o: signal.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@

rprotoserve.o: rprotoserve.cc
	$(CXX) $(ALL_CPPFLAGS) $(ALL_CXXFLAGS) $(MYCXXFLAGS) -c $< -o $@
	

app: rexp.pb.o message.o main.o display.o fileio.o signal.o rprotoserve.o
	$(CXX)  $^ -o ../rprotoserve ${MYCXXFLAGS} ${PKG_LIBS} ${RINC} ${RLIB}
