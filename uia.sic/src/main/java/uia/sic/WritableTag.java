package uia.sic;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Tag.
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

    private final String path;

    private final String name;

    private final String unit;

    private final boolean readonly;

    private Object value;

    private Date updateTime;

    private Object source;

    private final ArrayList<TagEventListener> listeners;

    /**
     * Create a tag..
     * 
     * @param path The path.
     * @param name The tag name.
     * @param unit The unit of value.
     */
    public WritableTag(String path, String name, String unit) {
        this(path, name, unit, null, false);
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
        this.listeners = new ArrayList<TagEventListener>();
        this.path = toPath(path);
        this.name = name;
        this.unit = unit;
        this.updateTime = new Date();
        this.value = value;
        this.readonly = readonly;
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

    /**
     * Get the path.
     * 
     * @return
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * Get tag name.
     * 
     * @return
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
     * @return
     */
    @Override
    public Object getValue() {
        return this.value;
    }

    /**
     * Setup tag value.
     * 
     * @param value
     */
    public void setValue(Object value) {
        if (this.readonly) {
            throw new IllegalArgumentException(toString() + " is readonly.");
        }
        writeValue(value);
    }

    public boolean valueEquals(Object value) {
        if (value != null && value.equals(this.value)) {
            return true;
        } else if (value == null && this.value == null) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @return
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
        return getPath() + "$" + getName() + "=" + getValue();
    }

    void raiseValueChanged() {
        System.out.println(this + ", listener count:" + this.listeners.size());
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
        throw new ClassCastException(getPath() + "$" + getName() + " must be " + typeName);
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
}
