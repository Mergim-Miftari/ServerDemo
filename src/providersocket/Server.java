package providersocket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The Server gets Requests from the Client via the Socket.
 * The Server analysis the Request and calls the methods specified in the Request at the Service (routing).
 * Than it returns an Response to the Client via the Socket.
 */
public class Server extends Thread {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Server.class);

    /** The Port to reach the Server. */
    private static final int STANDARD_PORT = 1099;

    /** The Port-Number. */
    private int portNumber;

    /** The Socket for the Server. The Communication will be between Socket and Socket. */
    private ServerSocket serverSocket;

    /** The Service. */
    private Service service;

    /**
     * the SimpleDelegatingServer gets configured with a service object and an additional class to
     * receive incoming connections and to delegate them to a new instance of the additional class
     * using the socket and the service object.
     *
     * @param portNumber port on which the ServerSocket will wait for connections
     * @param service    BDCCService object implementing the service. This will be passed through to
     *                   the new instance of the call handling thread class.
     * @throws IOException if any socket problem occurs
     */
    public Server(final int portNumber, Service service) throws IOException {
        this.portNumber = portNumber;
        serverSocket = new ServerSocket(portNumber);
        this.service = service;
    }

    /**
     * For details please ee the other constructor. This constructor just uses a default port number.
     *
     * @param service Service to be used during delegation
     * @throws IOException if any socket problem occurs
     */
    public Server(Service service) throws IOException {
        this(STANDARD_PORT, service);
    }

    /**
     * Goes into an endless loop accepting connections and delegating them to a separate thread.
     * this call blocks the control flow.
     *
     * @throws Exception Since this is just a finger exercise, whenever, whatever fails, we are out
     */
    public void foreverAcceptAndDelegate() throws Exception {
        start();
    }

    public void run() {
        try {
            logger.info("ready to go ...");
            while (true) {
                logger.info("Wait for a call ...");
                java.net.Socket clientSocket = serverSocket.accept();
                logger.info("got connection from " + clientSocket);
                new Connection(clientSocket, this);
                logger.info("ServeOneClient launched");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Problem in service socket wrapper. Terminating SimpleDelegatingServer.");
        }
    }

    /**
     * Calls the Service object.
     * @return the value.
     */
    public int getNextInt() {
        return service.getValue();
    }
}
