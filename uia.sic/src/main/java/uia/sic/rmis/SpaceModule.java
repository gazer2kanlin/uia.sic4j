package uia.sic.rmis;

import uia.sic.NodeSpace;

/**
 * 
 * @author Kyle
 * 
 */
public interface SpaceModule {

    public String getName();

    public void bind(NodeSpace space);

    public boolean start();

    public boolean stop();
}
