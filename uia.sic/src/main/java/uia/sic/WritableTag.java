package uia.sic;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Writable tag.
 * 
 * @author Kyle
 * 
 */
public class WritableTag implements Tag {

    private static final long serialVersionUID = 1421527197328199379L;

    private static final ThreadPoolExecutor TH_POOL_EXEC;

    static {
        TH_POOL_EXEC = new ThreadPoolExecutor(20, 40, 2, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(40));
    }

    private final ArrayList<TagEventListener> listeners;

    private final String path;

    private final String name;

    private final String id;

    private final String unit;

    private final boolean readonly;

    private Object value;

    private Date updateTime;

    private Object source;

    /**
     * Create a tag..
     * 
     * @param path The path.
     * @param name The tag name.
     * @param unit The unit of value.
     */
    public WritableTag(String path, String name, String unit) {
        this(path, name, unit, null, false, null);
    }

    /**
     * Create a writable tag..
     * 
     * @param path The path.
     * @param name The tag name.
     * @param unit The unit of value.
     * @param value The value.
     * @param readonly Read only or not.
     */
    public WritableTag(String path, String name, String unit, Object value, boolean readonly) {
        this(path, name, unit, value, readonly, null);
    }

    /**
     * Create a writable tag..
     * 
     * @param path The path.
     * @param name The tag name.
     * @param unit The unit of value.
     * @param value The value.
     * @param readonly Read only or not.
     * @param sourec Data object.
     */
    public WritableTag(String path, String name, String unit, Object value, boolean readonly, Object source) {
        this.listeners = new ArrayList<TagEventListener>();
        this.path = toPath(path);
        this.name = name;
        this.id = this.path + "$" + name;
        this.unit = unit;
        this.updateTime = new Date();
        this.value = value;
        this.readonly = readonly;
        this.source = source;
    }

    /**
     * Add event listener.
     * 
     * @param listener The event listener.
     */
    public void addEventListener(TagEventListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove event listener.
     * 
     * @param listener The event listener.
     */
    public void removeEventListener(TagEventListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * clear event listeners.
     */
    public void clearEventListeners() {
        this.listeners.clear();
    }

    public int getEventListenerCount() {
        return this.listeners.size();
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Get the path.
     * 
     * @return The path
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Get tag name.
     * 
     * @return The name
     */
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUnit() {
        return this.unit;
    }

    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * Get tag value.
     * 
     * @return The value.
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /**
     * Setup tag value.
     * 
     * @param value The value.
     */
    public void setValue(Object value) {
        if (this.readonly) {
            throw new IllegalArgumentException(toString() + " is readonly.");
        }
        writeValue(value);
    }

    /**
     * Compare value.
     * 
     * @param value Value compare with.
     * @return Equal or not.
     */
    public boolean valueEquals(Object value) {
        if (value != null && value.equals(this.value)) {
            return true;
        } else if (value == null && this.value == null) {
            return true;
        }
        return false;
    }

    /**
     * Get reference data.
     * 
     * @return Data object.
     */
    @Override
    public Object getSource() {
        return this.source;
    }

    /**
     * 
     * @param source
     */
    public void setSource(Object source) {
        this.source = source;
    }

    /**
     * Value of tag is read only or not.
     * 
     * @return Read only.
     */
    @Override
    public boolean isReadonly() {
        return this.readonly;
    }

    /**
     * Write value to tag and raise valueChanged event.
     * 
     * @param value Value.
     */
    void writeValue(Object value) {
        if (!valueEquals(value)) {
            this.value = value;
            this.updateTime = new Date();
            raiseValueChanged();
        }
    }

    @Override
    public String toString() {
        return this.id + "=" + getValue();
    }

    void raiseValueChanged() {
        for (final TagEventListener listener : this.listeners) {
            TH_POOL_EXEC.execute(new Runnable() {

                @Override
                public void run() {
                    listener.valueChanged(WritableTag.this); // thread
                }

            });
        }
    }

    protected void throwClassCastEx(String typeName) {
        throw new ClassCastException(this.id + " must be " + typeName);
    }

    static String toPath(String path) {
        String result = path;
        if (!path.endsWith("/")) {
            result += "/";
        }

        if (!result.startsWith("//")) {
            if (!result.startsWith("/")) {
                result = "//" + result;
            } else {
                result = "/" + result;
            }
        }
        return result;
    }

    /**
     * Find path and name.
     * 
     * @param pathName The path and name. Well format is {path}${name}. Path format is //{1}/{2}/.../.
     * @return Name is returned. If format is not correct, -1 is returned.
     */
    static String[] findPathAndName(String id) {
        int idx = id.lastIndexOf("//$");
        if (idx < 0) {
            return null;
        } else {
            return new String[] {
                    id.substring(0, idx + 1),
                    id.substring(idx + 2)
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        return o != null &&
                o instanceof WritableTag &&
                ((WritableTag) o).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
