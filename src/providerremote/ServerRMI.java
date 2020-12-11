package providerremote;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerRMI {
    public static final String REGISTRY_NAME = "serviceRMI";
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ServerRMI.class);

    public void foreverAcceptAndDelegate() throws Exception {
        ServiceRMI serviceRMI = new ServiceRMI();
        InterfaceServiceRMI remoteObject = (InterfaceServiceRMI) UnicastRemoteObject.exportObject(serviceRMI, 0);

        logger.info("Remote Object was created.");

        Registry registry = LocateRegistry.createRegistry(2020);
        registry.rebind(REGISTRY_NAME, remoteObject);
        logger.info("Registry was made");
        logger.info("Server is ready");
    }
}
