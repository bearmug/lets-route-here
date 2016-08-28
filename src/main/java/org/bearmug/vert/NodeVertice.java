package org.bearmug.vert;

public class NodeVertice implements Comparable {

    private final String name;
    private final long cost;
    private final NodeVertice parent;

    public NodeVertice(String name, NodeVertice parent, long cost) {
        this.name = name;
        this.cost = cost;
        this.parent = parent;
    }

    public NodeVertice(String name, long cost) {
        this(name, null, cost);
    }

    public long getCost() {
        return cost;
    }

    public NodeVertice getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        NodeVertice other = (NodeVertice) o;
        return Long.compare(getCost(), other.getCost());
    }

    @Override
    public final int hashCode() {
        return name.hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null || obj.getClass() != NodeVertice.class) {
            return false;
        }
        NodeVertice other = (NodeVertice) obj;
        return name.equals(other.getName());
    }
}
