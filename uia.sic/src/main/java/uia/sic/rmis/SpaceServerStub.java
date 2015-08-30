package uia.sic.rmis;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import uia.sic.Tag;

/**
 *
 * @author Kan
 *
 */
public class SpaceServerStub implements SpaceServer {

    private SpaceServer server;

    private String hostName;

    /**
     *
     * @param hostName
     */
    public SpaceServerStub(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public boolean register(String clientName, String hostName, String rmiName) throws RemoteException {
        try {
            return rebind(false) && this.server.register(clientName, hostName, rmiName);
        }
        catch (Exception ex) {
            rebind(true);
            return false;
        }
    }

    @Override
    public boolean register(String clientName, SpaceClient client) throws RemoteException {
        try {
            return rebind(false) && this.server.register(clientName, client);
        }
        catch (Exception ex) {
            rebind(true);
            return false;
        }
    }

    @Override
    public boolean unregister(String clientName) throws RemoteException {
        try {
            return rebind(false) && this.server.unregister(clientName);
        }
        catch (Exception ex) {
            rebind(true);
            return false;
        }
    }

    @Override
    public int listenTag(String clientName, String fullPath, String name) throws RemoteException {
        try {
            return rebind(false) ? this.server.listenTag(clientName, fullPath, name) : 0;
        }
        catch (Exception ex) {
            rebind(true);
            return 0;
        }
    }

    @Override
    public int listenTags(String clientName, String prePath) throws RemoteException {
        try {
            return rebind(false) ? this.server.listenTags(clientName, prePath) : 0;
        }
        catch (Exception ex) {
            rebind(true);
            return 0;
        }
    }

    @Override
    public int listenTags(String clientName, String prePath, String name) throws RemoteException {
        try {
            return rebind(false) ? this.server.listenTags(clientName, prePath, name) : 0;
        }
        catch (Exception ex) {
            rebind(true);
            return 0;
        }
    }

    @Override
    public Tag browseTag(String fullPath, String name) throws RemoteException {
        try {
            return rebind(false) ? this.server.browseTag(fullPath, name) : null;
        }
        catch (Exception ex) {
            rebind(true);
            return null;
        }
    }

    @Override
    public List<Tag> browseTags() throws RemoteException {
        try {
            return rebind(false) ? this.server.browseTags() : new ArrayList<Tag>();
        }
        catch (Exception ex) {
            rebind(true);
            return new ArrayList<Tag>();
        }
    }

    @Override
    public List<Tag> browseTags(String prePath) throws RemoteException {
        try {
            return rebind(false) ? this.server.browseTags(prePath) : new ArrayList<Tag>();
        }
        catch (Exception ex) {
            rebind(true);
            return new ArrayList<Tag>();
        }
    }

    @Override
    public List<Tag> browseTags(String prePath, String name) throws RemoteException {
        try {
            return rebind(false) ? this.server.browseTags(prePath, name) : new ArrayList<Tag>();
        }
        catch (Exception ex) {
            rebind(true);
            return new ArrayList<Tag>();
        }
    }

    @Override
    public int writeTag(String fullPath, String name, Object value) throws RemoteException {
        try {
            return rebind(false) ? this.server.writeTag(fullPath, name, value) : -1;
        }
        catch (Exception ex) {
            rebind(true);
            return -1;
        }
    }

    @Override
    public int writeTags(String prePath, String name, Object value) throws RemoteException {
        try {
            return rebind(false) ? this.server.writeTags(prePath, name, value) : -1;
        }
        catch (Exception ex) {
            rebind(true);
            return -1;
        }
    }

    private boolean rebind(boolean force) {
        if (!force && this.server != null) {
            return true;
        }

        try {
            this.server = (SpaceServer) LocateRegistry.getRegistry(this.hostName).lookup("SpaceServer");
            return true;
        }
        catch (Exception ex) {
            this.server = null;
            return false;
        }
    }
}
