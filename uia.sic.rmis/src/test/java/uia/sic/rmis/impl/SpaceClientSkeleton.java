package uia.sic.rmis.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import uia.sic.Tag;
import uia.sic.rmis.SpaceClient;

public class SpaceClientSkeleton extends UnicastRemoteObject implements SpaceClient {

    private static final long serialVersionUID = 8050138311256092251L;

    public SpaceClientSkeleton() throws RemoteException {
        super();
    }

    public void start() throws RemoteException, MalformedURLException {
        Naming.rebind("SpaceClient", this);
    }

    public void stop() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("SpaceClient");
    }

    @Override
    public void valueChanged(Tag tag) throws RemoteException {
        System.out.println(tag);
    }
    
    @Override
    public void alive()  throws RemoteException {
        System.out.println("alive");
    }
}
