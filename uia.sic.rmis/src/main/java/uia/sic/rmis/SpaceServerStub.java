package uia.sic.rmis;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SpaceServerStub {

    private final Registry registry;

    public SpaceServerStub(String hostName) throws RemoteException {
        this.registry = LocateRegistry.getRegistry(hostName);
    }

    public SpaceServer lookup() throws AccessException, RemoteException, NotBoundException {
        return (SpaceServer) this.registry.lookup("SpaceServer");
    }
}
