package uia.sic;

import java.io.Serializable;
import java.util.Date;

/**
 * Tag.
 * 
 * @author Kyle
 * 
 */
public interface Tag extends Serializable {

    /**
     * Get the path.
     * 
     * @return
     */
    public String getPath();

    /**
     * Get tag name.
     * 
     * @return
     */
    public String getName();

    /**
     * 
     * @return
     */
    public String getUnit();

    /**
     * 
     * @return
     */
    public Date getUpdateTime();

    /**
     * Get tag value.
     * 
     * @return
     */
    public Object getValue();

    /**
     * 
     * @return
     */
    public Object getSource();

    /**
     * Value of tag is read only or not.
     * 
     * @return Read only.
     */
    public boolean isReadonly();
}
