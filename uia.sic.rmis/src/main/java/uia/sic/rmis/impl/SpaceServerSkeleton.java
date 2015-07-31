package uia.sic.rmis.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uia.sic.NodeSpace;
import uia.sic.Tag;
import uia.sic.WritableTag;
import uia.sic.rmis.SpaceClient;
import uia.sic.rmis.SpaceModule;
import uia.sic.rmis.SpaceServer;

/**
 *
 * @author Kan
 *
 */
public class SpaceServerSkeleton extends UnicastRemoteObject implements SpaceServer {

    private static final Logger logger = Logger.getLogger(SpaceServerSkeleton.class);

    private static final long serialVersionUID = -452277332737341910L;

    private final NodeSpace space;

    private final HashMap<String, ClientTagEventListener> clientListeners;

    private final HashMap<String, SpaceModule> spaceModules;

    private boolean started;

    /**
     * Constructor.
     *
     * @param space Node space.
     * @throws RemoteException Raise if RMI initial failure.
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
     * Get node space.
     * @return Node space.
     */
    public NodeSpace getNodeSpace() {
        return this.space;
    }

    /**
     * Start space server.
     *
     * @throws RemoteException Raise if RMI initial failure.
     * @throws MalformedURLException Raise if RMI initial failure.
     */
    public void start() throws RemoteException, MalformedURLException {
        Naming.rebind("SpaceServer", this);
        for (SpaceModule module : this.spaceModules.values()) {
            module.start();
        }
        this.started = true;
        new Thread(new Runnable() {

            @Override
            public void run() {
                checkClientAlive();
            }

        }).start();
    }

    /**
     * Stop space server.
     *
     * @throws RemoteException Raise if RMI failure.
     * @throws MalformedURLException Raise if RMI failure.
     * @throws NotBoundException Raise if RMI failure.
     */
    public void stop() throws RemoteException, MalformedURLException, NotBoundException {
        Naming.unbind("SpaceServer");
        for (SpaceModule module : this.spaceModules.values()) {
            module.stop();
        }
        this.started = false;
        synchronized (this.clientListeners) {
            this.clientListeners.notifyAll();
        }
    }

    /**
     * Add module.
     * @param className Class name implements SpaceModule.
     * @return Add success or not.
     */
    public boolean addModule(String className) {
        try {
            SpaceModule module = (SpaceModule) Class.forName(className).newInstance();
            module.bind(this.space);
            this.spaceModules.put(module.getName(), module);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean register(String clientName, String hostName, String rmiName) throws RemoteException {
        try {
            String remoteURL = "rmi://" + hostName + ":1099/" + rmiName;
            logger.info(String.format("sic> %s register %s", clientName, remoteURL));
            SpaceClient client = (SpaceClient) Naming.lookup(remoteURL);
            return register(clientName, client);
        }
        catch (Exception ex) {
            throw new RemoteException("SpaceServer register failure", ex);
        }
    }

    @Override
    public boolean register(String clientName, SpaceClient client) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.remove(clientName);
        if (listener != null) {
            listener.clearTags();
        }

        if (client == null) {
            return false;
        }

        client.alive();
        this.clientListeners.put(clientName, new ClientTagEventListener(this, clientName, client));
        logger.info(String.format("sic> %s register %s", clientName, client));
        return true;
    }

    @Override
    public boolean unregister(String clientName) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.remove(clientName);
        if (listener == null) {
            return false;
        }

        listener.clearTags();
        logger.info(String.format("sic> %s unregister", clientName));
        return true;
    }

    @Override
    public int listenTag(String clientName, String fullPath, String propName) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.get(clientName);
        if (listener == null) {
            return -1;
        }

        WritableTag tag = this.space.single(fullPath, propName);
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
    public int listenTags(String clientName, String prePath, String propName) throws RemoteException {
        ClientTagEventListener listener = this.clientListeners.get(clientName);
        if (listener == null) {
            return -1;
        }

        List<WritableTag> tags = this.space.browse(prePath, propName);
        for (WritableTag tag : tags) {
            listener.addTag(tag);

        }
        return tags.size();
    }

    @Override
    public Tag browseTag(String fullPath, String propName) throws RemoteException {
        WritableTag tag = this.space.single(fullPath, propName);
        return tag == null ? null : convert(tag);
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
    public List<Tag> browseTags(String prePath, String propName) throws RemoteException {
        return convert(this.space.browse(prePath, propName));
    }

    @Override
    public int writeTag(String fullPath, String propName, Object value) throws RemoteException {
        WritableTag tag = this.space.single(fullPath, propName);
        if (tag == null) {
            logger.info("sic> " + fullPath + "$" + propName + " no tag!");
            return -1;
        }
        if (tag == null || tag.valueEquals(value)) {
            return 0;
        }

        try {
            tag.setValue(value);
            return 1;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return -2;
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

    private void checkClientAlive() {
        while (this.started) {
            try {
                synchronized (this.clientListeners) {
                    for (Map.Entry<String, ClientTagEventListener> e : this.clientListeners.entrySet()) {
                        e.getValue().alive();
                    }
                    this.clientListeners.wait(20000);
                }
            }
            catch (Exception ex) {

            }
        }
    }
}
