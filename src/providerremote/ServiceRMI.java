package providerremote;

import java.rmi.Remote;
import java.util.Random;

public class ServiceRMI implements InterfaceServiceRMI {

    public int getValue() {
        return new Random().nextInt(6)+1;
    }
}
