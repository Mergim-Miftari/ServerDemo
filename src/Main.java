import provider.Client;
import provider.Server;
import provider.Service;

/**
 * Main method for Server-Client-Demo.
 */
public class Main {
    /**
     * Main-Method
     * @param args
     */
    public static void main(String[] args) {
        startTheServer();
        startTheClient();
    }

    /**
     * Start the Server.
     */
    public static void startTheServer() {
        try {
            Server server = new Server(new Service());
            server.foreverAcceptAndDelegate();
        } catch (Exception e) {
            System.out.println("Some unexpected Error happened in Server-Main. ");
        }
    }

    /**
     * Start the Client.
     */
    public static void startTheClient() {
        Client client = new Client();
        client.startCalling();
    }
}
