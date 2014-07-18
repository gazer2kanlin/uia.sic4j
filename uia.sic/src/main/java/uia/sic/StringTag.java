package uia.sic;

/**
 * Tag.
 * 
 * @author Kyle
 * 
 */
public class StringTag extends WritableTag {

    private static final long serialVersionUID = -4289989040821099172L;

    /**
     * Create a writable tag..
     * 
     * @param path The path.
     * @param name The tag name.
     */
    public StringTag(String path, String name) {
        this(path, name, null, false);
    }

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     * @param value The value.
     * @param readonly Read only or not.
     */
    public StringTag(String path, String name, String value, boolean readonly) {
        super(path, name, "String", value, readonly);
    }

    /**
     * Write value to tag and raise valueChanged event.
     * 
     * @param value Value.
     */
    @Override
    void writeValue(Object value) {
        if (value instanceof String) {
            super.writeValue(value);
        } else {
            throwClassCastEx("string");
        }
    }
}
