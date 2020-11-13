package provider;

import exceptions.IllegalParameterException;
import exceptions.ServiceNotAvailableException;
import supporter.CheckingHelper;
import supporter.ServiceE;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * The Connection from the Server towards the Client-Socket.
 */
public class Connection extends Thread {
    /** Logger. */
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Connection.class);

    /** The Client-Socket, as well as the in and out put Stream. */
    private final java.net.Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /** The Server. */
    private Server server;

    /**
     * Creates new Connection to work on a single client request.
     * @param socket socket connected with the client
     * @param server server to be used for the request
     * @throws IOException when problems with the socket connection occur
     * @throws IllegalParameterException when called with null references
     */
    public Connection(final Socket socket, Server server) throws IOException, IllegalParameterException {
        //Check if the objects are not null.
        CheckingHelper.assertThatIsNotNull(socket, "socket");
        CheckingHelper.assertThatIsNotNull(server, "service");
        // Build IO streams
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        this.server = server;
        this.start();
    }

    /**
     * Disconnects / closes streams and socket.
     */
    public void disconnect() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            logger.warn("Problems during disconnect: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        
        Request request = null;
        Response response;
        
        try {
            request = (Request) in.readObject();

            ServiceE method = ServiceE.valueOf(request.getMethodToCall());

            switch (method) {
                case GET_NEXT_INTEGER: {
                    int value = server.getNextInt();
                    response = new Response(request, value);
                    break;
                }
                default:
                    // Create a response with a ServiceNotAvailableException
                    ServiceNotAvailableException noMethodException = new ServiceNotAvailableException("Method with name unknown. " + method);
                    response = new Response(request, noMethodException);
            }
        } catch (Exception e) {
            response = new Response(request, e);
        }

        try {
            out.writeObject(response);
        } catch (IOException e1) {
            logger.error("Problems writing the response: " + e1.getMessage());
        }
    }
}
