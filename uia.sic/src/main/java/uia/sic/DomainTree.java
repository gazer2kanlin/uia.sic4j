package uia.sic;

public class DomainTree {

    private final DomainNode rootNode;

    public DomainTree(String rootName) {
        if (rootName == null) {
            throw new NullPointerException("Name of DomainTree can't be null.");
        }
        this.rootNode = new DomainNode(rootName);
    }

    public DomainNode getRoot() {
        return this.rootNode;
    }

    public DomainNode browse(String nodePath) {
        if (nodePath == null) {
            return null;
        }

        String[] nodeTokens = nodePath.split("/");
        if (this.rootNode.getName().equals(nodeTokens[0])) {
            return browse(this.rootNode, nodeTokens, 1);
        } else {
            return null;
        }
    }

    private DomainNode browse(DomainNode node, String[] nodeTokens, int idx) {
        DomainNode sub = node.getSubNode(nodeTokens[idx]);
        if (sub == null) {
            return null;
        }

        if (idx == nodeTokens.length - 1) {
            return sub;
        }

        return browse(node, nodeTokens, idx + 1);
    }
}
