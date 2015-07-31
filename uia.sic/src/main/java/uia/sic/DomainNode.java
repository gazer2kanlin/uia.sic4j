package uia.sic;

import java.util.TreeMap;
import java.util.TreeSet;

public class DomainNode {

    private final String name;

    private final TreeMap<String, DomainNode> subNodes;

    private final TreeSet<Tag> tags;

    /**
     * Constructor.
     * 
     * @param name Name.
     */
    public DomainNode(String name) {
        this.name = name;
        this.subNodes = new TreeMap<String, DomainNode>();
        this.tags = new TreeSet<Tag>();
    }

    /**
     * Get name.
     * @return Name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add tag.
     * @param tag Tag.
     */
    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    /**
     * Remove tag.
     * @param tag Tag.
     */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    /**
     * Get tags.
     * @return Tags.
     */
    public Tag[] getTags() {
        return this.tags.toArray(new Tag[0]);
    }

    /**
     * Add node.
     * @param node Node.
     * @return Success or not.
     */
    public boolean addNode(DomainNode node) {
        if (this.subNodes.containsKey(node.getName())) {
            return false;
        }
        this.subNodes.put(node.getName(), node);
        return true;
    }

    /**
     * Remove node.
     * @param node Node.
     * @return Success or not.
     */
    public boolean removeNode(DomainNode node) {
        return this.subNodes.remove(node.getName()) != null;
    }

    /**
     * Get sub node.
     * @param name Name.
     * @return Sub node.
     */
    public DomainNode getSubNode(String name) {
        return this.subNodes.get(name);
    }
}
