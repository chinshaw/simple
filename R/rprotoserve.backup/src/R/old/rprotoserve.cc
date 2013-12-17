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


class Connection : public boost::enable_shared_from_this<Connection> {

public:
	typedef boost::shared_ptr<Connection> Pointer;

    static Pointer create(asio::io_service& io_service)
    {
        return Pointer(new Connection(io_service));
    }


    tcp::socket& get_socket()
    {
        return m_socket;
    }


private:

    tcp::socket m_socket;

    Connection(asio::io_service& io_service)
        : m_socket(io_service)
    {
    }


};


struct RProtoServe::RProtoServeImpl {
	tcp::socket m_socket;

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
		Connection::Pointer new_connection =
			Connection::create(acceptor.io_service());

		// Asynchronously wait to accept a new client
		//
		acceptor.async_accept(new_connection->get_socket(),
				boost::bind(&RProtoServeImpl::handle_accept, this, new_connection,
					asio::placeholders::error));
	}

	void handle_accept(Connection::Pointer connection,
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

