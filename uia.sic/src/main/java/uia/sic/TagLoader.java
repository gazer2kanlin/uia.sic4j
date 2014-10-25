package uia.sic;

import java.util.List;

/**
 * Tag loader.
 * 
 * @author Kyle
 * 
 */
public interface TagLoader {

    /**
     * Tags loaed into node space.
     * 
     * @return Tag list.
     */
    public List<WritableTag> load();
}
