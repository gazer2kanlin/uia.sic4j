package uia.sic.rmis;

import java.rmi.Remote;
import java.rmi.RemoteException;

import uia.sic.Tag;

public interface SpaceClient extends Remote {

    public void valueChanged(Tag tag) throws RemoteException;
}
