package uia.sic;

public abstract class DefaultIC extends IC implements TagEventListener {

    @Override
    protected void beforeStart() {
        for (WritableTag tag : this.inputs.values()) {
            tag.addEventListener(this);
        }
    }

    @Override
    protected void beforeStop() {
        for (WritableTag tag : this.inputs.values()) {
            tag.removeEventListener(this);
        }
    }
}
