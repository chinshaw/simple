#include "rprotoserve.h"
#include <boost/asio.hpp>
#include <boost/bind.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/cstdint.hpp>
#include <boost/enable_shared_from_this.hpp>



using namespace std;
namespace asio = boost::asio;
using asio::ip::tcp;
using boost::uint8_t;


bool started = false;


void start() {


	started = true;
}


struct RProtoServe::RProtoServeImpl {

	int m_port;
	tcp::acceptor acceptor;

	RProtoServeImpl(int port) {
		m_port = port;
	} 

	void start_accept() {
		// Create a new connection to handle a client. Passing a reference
		// to db to each connection poses no problem since the server is 
		// single-threaded.
		//
		DbConnection::Pointer new_connection =
			DbConnection::create(acceptor.io_service(), db);

		// Asynchronously wait to accept a new client
		//
		acceptor.async_accept(new_connection->get_socket(),
				boost::bind(&DbServerImpl::handle_accept, this, new_connection,
					asio::placeholders::error));
	}

	void handle_accept(DbConnection::Pointer connection,
			const boost::system::error_code& error)
	{

		// A new client has connected
		//
		if (!error) {
			// Start the connection
			//
			connection->start();

			// Accept another client
			//
			start_accept();
		}
	}
};



/**
 * Default constructor
 * Creates a new RProtoServeImpl and puts it into a scoped_ptr to be cleaned up.
 */
RProtoServe::RProtoServe() : dynamic_pointer(new RProtoServeImpl(5499)) {

}

/**
 * Constructor with port argument;
 * Creates a new RProtoServeImpl and puts it into a scoped_ptr to be cleaned up.
 */
RProtoServe::RProtoServe(int port) : dynamic_pointer(new RProtoServeImpl(port)) {

}


/**
 * Destructor
 */
RProtoServe::~RProtoServe() {
}

