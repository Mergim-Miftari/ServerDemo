package providerremote;

import providersocket.Request;
import providersocket.Response;
import providersocket.Socket;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI {
    /** The Hostname to reach the Server. */
    private static final String HOST_NAME = "localhost";

    public ClientRMI() {
        startCalling();
    }

    public void startCalling() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    try {
                        sleep(5000);

                        Registry registry = LocateRegistry.getRegistry(HOST_NAME, 2020);
                        InterfaceServiceRMI serviceRMI = (InterfaceServiceRMI) registry.lookup("serviceRMI");
                        int i = serviceRMI.getValue();

                        System.out.println("Client received a value. The value was " + i);
                    } catch (Exception e) {
                        System.out.println("Some Error with sleeping Thread in Client. ");
                    }
                }

            }
        }.start();
    }
}
