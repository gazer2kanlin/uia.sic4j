package uia.sic;


/**
 * Tag.
 * 
 * @author Kyle
 * 
 */
public class BooleanTag extends WritableTag {

    private static final long serialVersionUID = 6641394234025366154L;

    /**
     * Create a writable tag..
     * 
     * @param path The path.
     * @param name The tag name.
     */
    public BooleanTag(String path, String name) {
        this(path, name, true, false);
    }

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     * @param value The value.
     * @param readonly Read only or not.
     */
    public BooleanTag(String path, String name, boolean value, boolean readonly) {
        super(path, name, "Boolean", value, readonly);
    }

    /**
     * Write value to tag and raise valueChanged event.
     * 
     * @param value Value.
     */
    @Override
    void writeValue(Object value) {
        if (value instanceof Boolean) {
            super.writeValue(value);
        } else {
            throwClassCastEx("boolean");
        }
    }
}
