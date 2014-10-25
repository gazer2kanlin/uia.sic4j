package uia.sic;

/**
 * Tag event listener.
 * 
 * @author Kyle
 * 
 */
public interface TagEventListener {

    /**
     * Invoke when value of tag is changed.
     * 
     * @param tag
     */
    public void valueChanged(WritableTag tag);
}
