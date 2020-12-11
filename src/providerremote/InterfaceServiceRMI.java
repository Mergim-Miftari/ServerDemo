package providerremote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServiceRMI extends Remote {
    public int getValue() throws RemoteException;
}
