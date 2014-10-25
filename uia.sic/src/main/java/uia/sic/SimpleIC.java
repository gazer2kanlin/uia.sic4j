package uia.sic;

import java.util.HashMap;
import java.util.Set;

/**
 * Simple software IC.
 * 
 * @author Kyle
 * 
 */
public abstract class SimpleIC extends IC implements TagEventListener {

    protected final HashMap<String, WritableTag> inputs;

    protected final HashMap<String, WritableTag> outputs;

    /**
     * Constructor.
     * 
     */
    public SimpleIC() {
        this.inputs = new HashMap<String, WritableTag>();
        this.outputs = new HashMap<String, WritableTag>();
    }

    @Override
    public void start() {
        for (WritableTag tag : this.inputs.values()) {
            tag.addEventListener(this);
        }
    }

    @Override
    public void stop() {
        for (WritableTag tag : this.inputs.values()) {
            tag.removeEventListener(this);
        }
    }

    @Override
    public void bind(NodeSpace space) {
        for (String key : getInputTags()) {
            String pathName[] = WritableTag.findPathAndName(key);
            if (pathName == null) {
                this.inputs.put(key, null);
            } else {
                this.inputs.put(key, space.single(pathName[0], pathName[1]));
            }
        }
        for (String key : getOutputTags()) {
            String pathName[] = WritableTag.findPathAndName(key);
            if (pathName == null) {
                this.outputs.put(key, null);
            }
            this.outputs.put(key, space.single(pathName[0], pathName[1]));
        }
    }

    /**
     * Provide full pathname(path + $name) of tags to be input points in this IC.
     * 
     * @return tag list.
     */
    protected abstract Set<String> getInputTags();

    /**
     * Provide full pathname(path + $name) of tags to be output points in this IC.
     * 
     * @return tag list.
     */
    protected abstract Set<String> getOutputTags();
}
