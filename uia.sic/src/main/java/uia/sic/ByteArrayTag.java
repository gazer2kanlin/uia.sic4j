package uia.sic;

/**
 * Byte array tag.
 * 
 * @author Kyle
 * 
 */
public class ByteArrayTag extends WritableTag {

    private static final long serialVersionUID = -6922151413173590816L;

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     */
    public ByteArrayTag(String path, String name) {
        this(path, name, new byte[0], false);
    }

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     * @param value The value.
     * @param readonly Read only or not.
     */
    public ByteArrayTag(String path, String name, byte[] value, boolean readonly) {
        super(path, name, "Boolean", value, readonly);
    }

    /**
     * Write value to tag and raise valueChanged event.
     * 
     * @param value Value.
     */
    @Override
    void writeValue(Object value) {
        if (value instanceof byte[]) {
            super.writeValue(value);
        } else {
            throwClassCastEx("byte array");
        }
    }
}
