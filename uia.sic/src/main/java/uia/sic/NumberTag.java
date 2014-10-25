package uia.sic;

/**
 * Number tag.
 * 
 * @author Kyle
 * 
 */
public class NumberTag extends WritableTag {

    private static final long serialVersionUID = 5156638506689522891L;

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     */
    public NumberTag(String path, String name) {
        this(path, name, 0, false);
    }

    /**
     * Create a tag.
     * 
     * @param path The path.
     * @param name The tag name.
     * @param value The value.
     * @param readonly Read only or not.
     */
    public NumberTag(String path, String name, Number value, boolean readonly) {
        super(path, name, "Number", value, readonly);
    }

    /**
     * Write value to tag and raise valueChanged event.
     * 
     * @param value Value.
     */
    @Override
    void writeValue(Object value) {
        if (value instanceof Number) {
            super.writeValue(value);
        } else {
            throwClassCastEx("number");
        }
    }
}
