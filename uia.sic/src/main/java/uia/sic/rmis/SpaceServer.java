package uia.sic.rmis;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import uia.sic.Tag;

/**
 * 
 * @author Kyle
 * 
 */
public interface SpaceServer extends Remote {

    /**
     * Register a client service.
     * 
     * @param clientName The client name.
     * @param hostName The host runs SpaceClient RMI service.
     * @param rmiLookupName The SpaceClient RMI binding name.
     * @return Success or not.
     * @throws RemoteException
     */
    public boolean register(String clientName, String hostName, String rmiLookupName) throws RemoteException;

    /**
     * Unregister a client service.
     * 
     * @param clientName The client name.
     * @return Success or not.
     * @throws RemoteException
     */
    public boolean unregister(String clientName) throws RemoteException;

    /**
     * Listen one tag in space.
     * 
     * @param clientName Client name.
     * @param fullPath The full path.
     * @param name Tag name.
     * @return 0: no tag. 1: success. -1: no this client service.
     * @throws RemoteException
     */
    public int listenTag(String clientName, String fullPath, String name) throws RemoteException;

    /**
     * Listen tags in space.
     * 
     * @param clientName
     * @param prePath The pre-path.
     * @return success or not.
     * @throws RemoteException
     */
    public int listenTags(String clientName, String prePath) throws RemoteException;

    /**
     * Listen tags in space.
     * 
     * @param clientName Client name.
     * @param prePath The pre-path.
     * @param name Tag name.
     * @return success or not.
     * @throws RemoteException
     */
    public int listenTags(String clientName, String prePath, String name) throws RemoteException;

    /**
     * Browse one tag in node space.
     * 
     * @param fullPath
     * @param name Tag name.
     * @return Tag.
     * @throws RemoteException
     */
    public Tag browseTag(String fullPath, String name) throws RemoteException;

    /**
     * Browse all tags in node space.
     * 
     * @return Tags.
     * @throws RemoteException
     */
    public List<Tag> browseTags() throws RemoteException;

    /**
     * Browse tags in node space.
     * 
     * @param prePath The pre-path.
     * @return Tags.
     * @throws RemoteException
     */
    public List<Tag> browseTags(String prePath) throws RemoteException;

    /**
     * Browse tags in node space.
     * 
     * @param prePath The pre-path.
     * @param name Tag name.
     * @return Tags.
     * @throws RemoteException
     */
    public List<Tag> browseTags(String prePath, String name) throws RemoteException;

    /**
     * Write value into one tag.
     * 
     * @param fullPath Full path.
     * @param name Tag name.
     * @param value Value.
     * @return 0: no tag or same value. 1: success. -1: internal exception.
     * @throws RemoteException
     */
    public int writeTag(String fullPath, String name, Object value) throws RemoteException;

    /**
     * 
     * @author Kyle
     * 
     */
    public final static class Stub {

        private Stub() {
        }

        public static SpaceServer lookup(String hostName) throws AccessException, RemoteException, NotBoundException {
            return (SpaceServer) LocateRegistry.getRegistry(hostName).lookup("SpaceServer");
        }
    }

}
