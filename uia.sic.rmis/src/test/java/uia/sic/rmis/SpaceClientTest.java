package uia.sic.rmis;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import org.junit.Test;

import uia.sic.Tag;

public class SpaceClientTest {

    public SpaceClientTest() {
        try {
            LocateRegistry.createRegistry(1099);
            System.setProperty(
                    "java.security.policy",
                    System.getProperty("user.dir") + "/pis.policy");
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            System.setProperty(
                    "java.rmi.server.codebase",
                    "file://" + System.getProperty("user.dir") + "/target/classes/");
        } catch (Exception ex) {
        }
    }

    @Test
    public void testEnv() throws Exception {
        Registry registry = LocateRegistry.getRegistry("localhost");
        for (String serv : registry.list()) {
            System.out.println(serv);
        }
    }

    @Test
    public void testCase1() throws Exception {
        // start server
        new SpaceServerSkeleton(new SampleTagLoader()).start();

        // start client
        new SpaceClientSkeleton().start();

        // get server stub
        SpaceServerStub stub = new SpaceServerStub("localhost");
        SpaceServer server = stub.lookup();
        System.out.println("server> " + server);

        // browse tags
        List<Tag> tags = server.browseTags("//s200/");
        for (Tag tag : tags) {
            System.out.println(tag);
        }

        // register client to server
        System.out.println("register:" + server.register("ABC", "localhost", "SpaceClient"));
        System.out.println("register:" + server.register("ABCd", "localhost", "SpaceClient1"));

        // listen tags
        System.out.println("listen:" + server.listenTag("ABC", "//s200/PID/M4/0001/", "online"));
        System.out.println("listen:" + server.listenTag("ABCd", "//s200/PID/M4/0001/", "online"));

        // change value
        System.out.println("write1:" + server.writeTag("//s200/PID/M4/0001/", "online", false));
        Thread.sleep(1000);
        System.out.println("write2:" + server.writeTag("//s200/PID/M4/0001/", "online", false));
        Thread.sleep(1000);
        System.out.println("write3:" + server.writeTag("//s200/PID/M4/0001/", "online", true));
        Thread.sleep(1000);
        System.out.println("write4:" + server.writeTag("//s200/PID/M4/0001/", "online1", false));
        Thread.sleep(1000);

        // unregister
        System.out.println("unregister:" + server.unregister("ABC"));

        // change value
        System.out.println("write5:" + server.writeTag("//s200/PID/M4/0001/", "online", true));
        Thread.sleep(1000);

    }
}
