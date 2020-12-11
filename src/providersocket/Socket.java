package providersocket;

import exceptions.IllegalParameterException;
import exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * The Socket is the interface between the Client and the Server.
 */
public class Socket {
    /** The hostname and the portNumber. */
    private String hostname;
    private int portNumber;

    /** The socket and the in- and out-put streams. */
    private java.net.Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /** The logger. */
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Socket.class);

    /**
     * Constructor
     * @param hostname the name of the host
     * @param portNumber the number of the port
     */
    public Socket(String hostname, int portNumber) {
        this.hostname = hostname;
        this.portNumber = portNumber;
    }

    /**
     * Method to build the connection.
     */
    private void connectToService() throws ServiceNotAvailableException {
        try {
            socket = new java.net.Socket(hostname, portNumber);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream((socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new ServiceNotAvailableException("Cannot connect to host " + hostname + " with port " + portNumber + ".", ex);
        }
    }

    /**
     * Method to break the connection.
     */
    private void disconnectFromService() {
        try {
            if (out != null) {
                out.close();
                out = null;
            }
            if (in != null) {
                in.close();
                in = null;
            }

            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException ex) {
            // Problems disconnecting should not terminate the interaction. So we only log the problem.
            logger.warn("Problems disconnecting from service: " + ex.getMessage());
        }
    }

    public Response sendAndGetResponse(final Request request) throws ServiceNotAvailableException {
        // send request and wait for the response
        try {
            connectToService();
            logger.debug("sending request " + request);
            out.writeObject(request);
            logger.debug("wait for response ...");
            Response response = (Response) in.readObject();
            logger.debug("Got response " + response);
            disconnectFromService();
            return response;
        } catch (Exception ex) {
            throw new ServiceNotAvailableException("Communication problem: " + ex.getMessage(), ex);
        }
    }

    /**
     * This method analysis the Exception, which we get from the response.
     * @param response
     * @throws IllegalParameterException
     * @throws ServiceNotAvailableException
     */
    private void rethrowStandardExceptions(final Response response) throws IllegalParameterException, ServiceNotAvailableException {
        Exception exceptionFromRemote = response.getExceptionObject();

        // check all acceptable exception types and rethrow
        if (exceptionFromRemote instanceof IllegalParameterException) {
            throw (IllegalParameterException) exceptionFromRemote;
        }

        if (exceptionFromRemote instanceof ServiceNotAvailableException) {
            throw (ServiceNotAvailableException) exceptionFromRemote;
        }

        rethrowUnexpectedException(exceptionFromRemote);
    }

    /**
     * This method provides the posibility to map other Exceptions to the ServiceNotAvailableException.
     * @param exceptionFromRemote other Exception
     * @throws ServiceNotAvailableException new Exception
     */
    private void rethrowUnexpectedException(final Exception exceptionFromRemote) throws
            ServiceNotAvailableException {
        // if we reach this part, than the exception is an exception we do not expect!
        logger.error("WTF - Unknown exception object in response: " + exceptionFromRemote);
        exceptionFromRemote.printStackTrace();
        throw new ServiceNotAvailableException("Unknown exception received in response object.",
                exceptionFromRemote);
    }
}
