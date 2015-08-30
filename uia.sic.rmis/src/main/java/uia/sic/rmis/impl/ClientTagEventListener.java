package uia.sic.rmis.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import uia.sic.TagEventListener;
import uia.sic.WritableTag;
import uia.sic.rmis.SpaceClient;

class ClientTagEventListener implements TagEventListener {

    private static Logger logger = Logger.getLogger(ClientTagEventListener.class);

    private final SpaceServerSkeleton server;

    private final String name;

    private final SpaceClient client;

    private final ArrayList<WritableTag> tags;

    public ClientTagEventListener(SpaceServerSkeleton server, String name, SpaceClient client) {
        this.server = server;
        this.name = name;
        this.client = client;
        this.tags = new ArrayList<WritableTag>();
    }

    public boolean alive() {
        try {
            this.client.alive();
            logger.debug(String.format("sic> %s> alive", this.name));
            return true;
        }
        catch (Exception ex) {
            logger.error(String.format("sic> %s> not alive", this.name));
            return true;
        }
    }

    public void addTag(WritableTag tag) {
        tag.addEventListener(this);
        this.tags.add(tag);
    }

    public void removeTag(WritableTag tag) {
        tag.removeEventListener(this);
        this.tags.remove(tag);
    }

    public void clearTags() {
        for (WritableTag tag : this.tags) {
            tag.removeEventListener(this);
        }
    }

    @Override
    public void valueChanged(final WritableTag tag) {
        try {
            ReadonlyTag roTag = new ReadonlyTag(tag.getPath(), tag.getName());
            roTag.setUnit(tag.getUnit());
            roTag.setValue(tag.getValue());
            roTag.setSource(tag.getSource());
            roTag.setUpdateTime(tag.getUpdateTime());
            roTag.setReadonly(tag.isReadonly());
            ClientTagEventListener.this.client.valueChanged(roTag);
        }
        catch (Exception ex) {
            logger.error(String.format("sic> %s> notify failure. maybe disconnect or broken.", ClientTagEventListener.this.name));
            unregisteredSelf();
        }
    }

    private void unregisteredSelf() {
        try {
            logger.info(String.format("sic> %s unregister self", this.name));
            this.server.unregister(this.name);
        }
        catch (Exception ex) {

        }
    }
}
