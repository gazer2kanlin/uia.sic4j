package uia.sic.rmis;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uia.sic.NodeSpace;
import uia.sic.Tag;
import uia.sic.TagLoader;
import uia.sic.WritableTag;

public class SpaceServerSkeleton extends UnicastRemoteObject implements SpaceServer {

    private static final long serialVersionUID = -452277332737341910L;

    private final NodeSpace space;

    private final HashMap<String, ClientTagEventListener> clientListeners;

    public SpaceServerSkeleton(TagLoader loader) throws RemoteException {
        super();
        this.space = new NodeSpace(loader);
        this.clientListeners = new HashMap<String, ClientTagEventListener>();
    }

    public void start() throws RemoteException, MalformedURLException {
        Naming.rebind("SpaceServer", this);
    }

    public void stop() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("SpaceServer");
    }

    @Override
    public boolean register(String clientName, String hostName, String lookupName) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(hostName);
            SpaceClient stub = (SpaceClient) registry.lookup(lookupName);
            this.clientListeners.put(clientName, new ClientTagEventListener(stub));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean unregister(String clientName) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.remove(clientName);
        if (listener == null) {
            return false;
        }

        listener.clearTags();
        return true;
    }

    @Override
    public int listenTag(String clientName, String fullPath, String name) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.get(clientName);
        if (listener == null) {
            return -1;
        }

        WritableTag tag = this.space.single(fullPath, name);
        if (tag == null) {
            return 0;
        }

        listener.addTag(tag);
        return 1;
    }

    @Override
    public boolean listenTags(String clientName, String prePath) throws RemoteException {
        // TODO:
        return false;
    }

    @Override
    public boolean listenTags(String clientName, String prePath, String name) throws RemoteException {
        // TODO:
        return false;
    }

    @Override
    public WritableTag browseTag(String fullPath, String name) throws RemoteException {
        return this.space.single(fullPath, name);
    }

    @Override
    public List<Tag> browseTags() throws RemoteException {
        return convert(this.space.browseAll());
    }

    @Override
    public List<Tag> browseTags(String prePath) throws RemoteException {
        return convert(this.space.browse(prePath));
    }

    @Override
    public List<Tag> browseTags(String prePath, String name) throws RemoteException {
        return convert(this.space.browse(prePath, name));
    }

    @Override
    public int writeTag(String fullPath, String name, Object value) throws RemoteException {
        WritableTag tag = this.space.single(fullPath, name);
        if (tag == null || tag.valueEquals(value)) {
            return 0;
        }
        try {
            tag.setValue(value);
            return 1;
        } catch (Exception ex) {
            return -1;
        }
    }

    private List<Tag> convert(List<WritableTag> tags) {
        ArrayList<Tag> result = new ArrayList<Tag>();
        for (WritableTag tag : tags) {
            result.add(tag);
        }
        return result;
    }
}
