package uia.sic.rmis.impl;

import java.util.Date;

import uia.sic.Tag;

/**
 * Read only tag implementation.
 *
 * @author Kyle
 *
 */
public class ReadonlyTag implements Tag {

    private static final long serialVersionUID = -902324986650928325L;

    private String path;

    private String name;

    private String id;

    private String unit;

    private Object value;

    private Object source;

    private boolean readonly;

    private Date updateTime;

    ReadonlyTag(String path, String name) {
        this.path = path;
        this.name = name;
        this.id = this.path + "$" + this.name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getUnit() {
        return this.unit;
    }

    void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    void setValue(Object value) {
        this.value = value;
    }

    @Override
    public Object getSource() {
        return this.source;
    }

    void setSource(Object source) {
        this.source = source;
    }

    @Override
    public boolean isReadonly() {
        return this.readonly;
    }

    void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public Date getUpdateTime() {
        return this.updateTime;
    }

    void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return getPath() + "$" + getName() + "=" + getValue();
    }

    @Override
    public boolean equals(Object o) {
        return o != null &&
                o instanceof ReadonlyTag &&
                ((ReadonlyTag) o).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
