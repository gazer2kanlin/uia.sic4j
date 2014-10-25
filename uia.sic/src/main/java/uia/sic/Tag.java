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
     * Get id. Id is path + "$" + name.
     * 
     * @return The id.
     */
    public String getId();

    /**
     * Get the path.
     * 
     * @return The path.
     */
    public String getPath();

    /**
     * Get tag name.
     * 
     * @return The name.
     */
    public String getName();

    /**
     * Get the unit.
     * 
     * @return The unit.
     */
    public String getUnit();

    /**
     * Get update time.
     * 
     * @return Update time.
     */
    public Date getUpdateTime();

    /**
     * Get tag value.
     * 
     * @return The value.
     */
    public Object getValue();

    /**
     * Get reference data.
     * 
     * @return Data object.
     */
    public Object getSource();

    /**
     * Value of tag is read only or not.
     * 
     * @return Read only.
     */
    public boolean isReadonly();
}
