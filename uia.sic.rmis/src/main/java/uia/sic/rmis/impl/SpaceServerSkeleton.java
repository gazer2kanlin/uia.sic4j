package uia.sic.rmis.impl;

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
import uia.sic.WritableTag;
import uia.sic.rmis.SpaceClient;
import uia.sic.rmis.SpaceModule;
import uia.sic.rmis.SpaceServer;

public class SpaceServerSkeleton extends UnicastRemoteObject implements SpaceServer {

    private static final long serialVersionUID = -452277332737341910L;

    private final NodeSpace space;

    private final HashMap<String, ClientTagEventListener> clientListeners;

    private final HashMap<String, SpaceModule> spaceModules;

    /**
     * 
     * @param space
     * @throws RemoteException
     */
    public SpaceServerSkeleton(NodeSpace space) throws RemoteException {
        super();
        if (space == null) {
            throw new NullPointerException("space can't be null.");
        }
        this.space = space;
        this.clientListeners = new HashMap<String, ClientTagEventListener>();
        this.spaceModules = new HashMap<String, SpaceModule>();
    }

    /**
     * 
     * @return
     */
    public NodeSpace getNodeSpace() {
        return this.space;
    }

    /**
     * 
     * @throws RemoteException
     * @throws MalformedURLException
     */
    public void start() throws RemoteException, MalformedURLException {
        Naming.rebind("SpaceServer", this);
        for (SpaceModule module : this.spaceModules.values()) {
            module.start();
        }
    }

    /**
     * 
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public void stop() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("SpaceServer");
        for (SpaceModule module : this.spaceModules.values()) {
            module.stop();
        }
    }

    public boolean addModule(String className) {
        try {
            SpaceModule module = (SpaceModule) Class.forName(className).newInstance();
            module.bind(this.space);
            this.spaceModules.put(module.getName(), module);
            return true;
        } catch (Exception ex) {
            return false;
        }
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
    public int listenTags(String clientName, String prePath) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.get(clientName);
        if (listener == null) {
            return -1;
        }

        List<WritableTag> tags = this.space.browse(prePath);
        for (WritableTag tag : tags) {
            listener.addTag(tag);

        }
        return tags.size();
    }

    @Override
    public int listenTags(String clientName, String prePath, String name) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.get(clientName);
        if (listener == null) {
            return -1;
        }

        List<WritableTag> tags = this.space.browse(prePath, name);
        for (WritableTag tag : tags) {
            listener.addTag(tag);

        }
        return tags.size();
    }

    @Override
    public Tag browseTag(String fullPath, String name) throws RemoteException {
        return convert(this.space.single(fullPath, name));
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
            result.add(convert(tag));
        }
        return result;
    }

    private Tag convert(WritableTag tag) {
        ReadonlyTag roTag = new ReadonlyTag(tag.getPath(), tag.getName());
        roTag.setUnit(tag.getUnit());
        roTag.setValue(tag.getValue());
        roTag.setSource(tag.getSource());
        roTag.setUpdateTime(tag.getUpdateTime());
        roTag.setReadonly(tag.isReadonly());
        return roTag;
    }
}
