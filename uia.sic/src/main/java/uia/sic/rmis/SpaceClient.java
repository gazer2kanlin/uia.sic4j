package uia.sic.rmis;

import java.rmi.Remote;
import java.rmi.RemoteException;

import uia.sic.Tag;

/**
 * Space client RMI interface.
 * 
 * @author Kyle
 * 
 */
public interface SpaceClient extends Remote {

    /**
     * Invoke when value of one tag is changed.
     * 
     * @param tag The tag.
     * @throws RemoteException RMI remote exception.
     */
    public void valueChanged(Tag tag) throws RemoteException;
}
