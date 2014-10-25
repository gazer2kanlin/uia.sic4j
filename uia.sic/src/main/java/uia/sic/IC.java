package uia.sic;

/**
 * Software IC.
 * 
 * @author Kyle
 * 
 */
public abstract class IC {

    /**
     * Start this IC.
     */
    public abstract void start();

    /**
     * Stop this IC.
     */
    public abstract void stop();

    /**
     * Bind tags of space into this IC.
     * 
     * @param space
     */
    public abstract void bind(NodeSpace space);

    /**
     * Write value into tag.
     * 
     * @param tag The tag.
     * @param value New value.
     */
    protected void writeValue(WritableTag tag, Object value) {
        if (tag != null) {
            tag.writeValue(value);
        }
    }
}
