package providersocket;

/**
 * The Client wants to use the Service.
 * To do this, he needs to send Requests to the Server via the Socket.
 * Then the Server uses the Service and sends a Response to the Client via the Socket.
 */
public class Client extends Thread {
    /** The Port to reach the Server. */
    private static final int STANDARD_PORT = 1099;

    /** The Hostname to reach the Server. */
    private static final String HOST_NAME = "localhost";

    private Socket ownSocket;

    public Client() {
        ownSocket = new Socket(HOST_NAME, STANDARD_PORT);
    }

    public void startCalling() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    try {
                        sleep(5000);
                        Response r = ownSocket.sendAndGetResponse(new Request("GET_NEXT_INTEGER"));
                        int i = (int) r.getReturnObject();
                        System.out.println("Client received a value. The value was " + i);
                    } catch (Exception e) {
                        System.out.println("Some Error with sleeping Thread in Client. ");
                    }
                }

            }
        }.start();
    }
}
