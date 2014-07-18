package uia.sic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

/**
 * 
 * @author Kyle
 * 
 */
public class NodeSpace {

    private final TagGroup group;

    private final TreeMap<String, ArrayList<WritableTag>> pathTags;

    private final ArrayList<WritableTag> tags;

    /**
     * Constructor.
     * 
     * @param loader The tag loader.
     */
    public NodeSpace(TagLoader loader) {
        this.group = new TagGroup("");
        this.pathTags = new TreeMap<String, ArrayList<WritableTag>>();

        List<WritableTag> tags = loader.load();
        for (WritableTag tag : tags) {
            ArrayList<WritableTag> pool = this.pathTags.get(tag.getPath());
            if (pool == null) {
                pool = new ArrayList<WritableTag>();
                this.pathTags.put(tag.getPath(), pool);
            }
            pool.add(tag);
            String[] branches = pathToTokens(tag.getPath());
            build(this.group, branches, 0, tag);
        }

        this.tags = new ArrayList<WritableTag>();
        Collection<ArrayList<WritableTag>> tagsList = this.pathTags.values();
        for (ArrayList<WritableTag> ts : tagsList) {
            this.tags.addAll(ts);
        }
    }

    /**
     * Browse a tag with full path and name.
     * 
     * @param fullPath The full path.
     * @param name The tag name.
     * @return Null if not found.
     */
    public WritableTag single(String fullPath, String name) {
        ArrayList<WritableTag> tags = this.pathTags.get(WritableTag.toPath(fullPath));
        for (WritableTag tag : tags) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Browse all tags in node space.
     * 
     * @return all tags.
     */
    public List<WritableTag> browseAll() {
        return this.tags;
    }

    /**
     * Browse tags starting with specific path.
     * 
     * @param prePath The path.
     * @return Tags.
     */
    public List<WritableTag> browse(String prePath) {
        String[] branches = pathToTokens(WritableTag.toPath(prePath));
        return toTags(browse(this.group, branches, 0));
    }

    /**
     * Browse tags starting with specific path and tag name.
     * 
     * @param prePath The path.
     * @param name The tag name.
     * @return Tags.
     */
    public List<WritableTag> browse(String prePath, String name) {
        String[] branches = pathToTokens(WritableTag.toPath(prePath));
        return toTags(browse(this.group, branches, 0), name);
    }

    private TagGroup browse(TagGroup group, String[] branches, int index) {
        if (branches.length == index) {
            return group;
        }

        TagGroup subGroup = group.subGroups.get(branches[index]);
        if (subGroup != null) {
            return browse(subGroup, branches, index + 1);
        } else {
            return null;
        }
    }

    private ArrayList<WritableTag> toTags(TagGroup group) {
        if (group == null) {
            return new ArrayList<WritableTag>();
        }

        ArrayList<WritableTag> result = new ArrayList<WritableTag>();
        result.addAll(group.tags);
        for (TagGroup subGroup : group.subGroups.values()) {
            result.addAll(toTags(subGroup));
        }
        return result;
    }

    private ArrayList<WritableTag> toTags(TagGroup group, String name) {
        if (group == null) {
            return new ArrayList<WritableTag>();
        }

        ArrayList<WritableTag> result = new ArrayList<WritableTag>();
        for (WritableTag tag : group.tags) {
            if (name.equals(tag.getName())) {
                result.add(tag);
            }
        }
        for (TagGroup subGroup : group.subGroups.values()) {
            result.addAll(toTags(subGroup, name));
        }
        return result;
    }

    private void build(TagGroup group, String[] branches, int index, WritableTag tag) {
        if (branches.length == index) {
            group.tags.add(tag);
            return;
        }

        String branch = branches[index];
        TagGroup subGroup = group.subGroups.get(branch);
        if (subGroup == null) {
            subGroup = new TagGroup(branch);
            group.subGroups.put(branch, subGroup);
        }

        build(subGroup, branches, index + 1, tag);

    }

    private static String[] pathToTokens(String path) {
        return path.length() == 2 ?
                new String[0] :
                path.substring(2, path.length() - 1).split("/");
    }

    /**
     * 
     * @author Kyle
     * 
     */
    class TagGroup {

        final String name;

        final TreeMap<String, TagGroup> subGroups;

        final ArrayList<WritableTag> tags;

        /**
         * 
         * @param name
         */
        public TagGroup(String name) {
            this.name = name;
            this.subGroups = new TreeMap<String, TagGroup>();
            this.tags = new ArrayList<WritableTag>();
        }
    }

}
