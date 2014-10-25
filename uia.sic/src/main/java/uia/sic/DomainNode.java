package uia.sic;

import java.util.TreeMap;
import java.util.TreeSet;

public class DomainNode {

    private final String name;

    private final TreeMap<String, DomainNode> subNodes;

    private final TreeSet<Tag> tags;

    /**
     * 
     * @param name
     */
    public DomainNode(String name) {
        this.name = name;
        this.subNodes = new TreeMap<String, DomainNode>();
        this.tags = new TreeSet<Tag>();
    }

    public String getName() {
        return this.name;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public Tag[] getTags() {
        return this.tags.toArray(new Tag[0]);
    }

    public boolean addNode(DomainNode node) {
        if (this.subNodes.containsKey(node.getName())) {
            return false;
        }
        this.subNodes.put(node.getName(), node);
        return true;
    }

    public boolean removeNode(DomainNode node) {
        return this.subNodes.remove(node.getName()) != null;
    }

    public DomainNode getSubNode(String name) {
        return this.subNodes.get(name);
    }
}
