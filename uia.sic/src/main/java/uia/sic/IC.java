package uia.sic;

import java.util.HashMap;
import java.util.Set;

public abstract class IC {

    protected final HashMap<String, WritableTag> inputs;

    protected final HashMap<String, WritableTag> outputs;

    public IC() {
        this.inputs = new HashMap<String, WritableTag>();
        this.outputs = new HashMap<String, WritableTag>();
    }

    public Set<String> getInputNames() {
        return this.inputs.keySet();
    }

    public Set<String> getOutputNames() {
        return this.outputs.keySet();
    }

    public void start() {
        beforeStart();
    }

    public void stop() {
        beforeStop();
    }

    public void bindInput(String name, WritableTag tag) {
        if (this.inputs.containsKey(name)) {
            this.inputs.put(name, tag);
        }
    }

    public void bindOutput(String name, WritableTag tag) {
        if (this.outputs.containsKey(name)) {
            this.outputs.put(name, tag);
        }
    }

    public abstract void bind(NodeSpace space);

    protected void writeValue(WritableTag tag, Object value) {
        tag.writeValue(value);
    }

    protected abstract void beforeStart();

    protected abstract void beforeStop();

    protected abstract void initialNamespace();
}
