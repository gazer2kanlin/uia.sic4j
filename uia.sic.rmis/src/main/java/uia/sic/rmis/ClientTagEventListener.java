package uia.sic.rmis;

import java.util.ArrayList;

import uia.sic.ReadonlyTag;
import uia.sic.TagEventListener;
import uia.sic.WritableTag;

public class ClientTagEventListener implements TagEventListener {

    private final SpaceClient client;

    private final ArrayList<WritableTag> tags;

    public ClientTagEventListener(SpaceClient client) {
        this.client = client;
        this.tags = new ArrayList<WritableTag>();
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
    public void valueChanged(WritableTag tag) {
        try {
            ReadonlyTag roTag = new ReadonlyTag();
            roTag.setPath(tag.getPath());
            roTag.setName(tag.getName());
            roTag.setUnit(tag.getUnit());
            roTag.setValue(tag.getValue());
            roTag.setSource(tag.getSource());
            roTag.setUpdateTime(tag.getUpdateTime());
            roTag.setReadonly(tag.isReadonly());
            this.client.valueChanged(roTag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
