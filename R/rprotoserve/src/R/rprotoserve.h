#ifndef RProtoServeH
#define RProtoServeH


#include <string>
#include <iostream>


class RProtoServe{


public:
	RProtoServe();
	RProtoServe(int port);


	int getPort();
	void setPort(int port);
	

	void start();
	void stop();

	// Eval a script or command
	void eval(char *command);


private:
	int port;
	bool started;
};

#endif
