#ifndef RProtoServeH
#define RProtoServeH


#include <string>
#include <iostream>
#include <boost/asio.hpp>
#include <boost/shared_ptr.hpp>


class RProtoServe{


public:
	RProtoServe();
	RProtoServe(int port);
	~RProtoServe();


	int getPort();
	void setPort(int port);
	

	void start();
	void stop();

	// Eval a script or command
	void eval(char *command);

private:
	int port;
	bool started;
	struct RProtoServeImpl;
	// scoped pointer is guaranteed to be deleted.
	boost::scoped_ptr<RProtoServeImpl> dynamic_pointer;
};

#endif
