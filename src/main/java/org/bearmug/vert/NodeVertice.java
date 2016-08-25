package org.bearmug.vert;

public class NodeVertice extends Vertice implements Comparable {

    final long cost;
    final NodeVertice parent;

    public NodeVertice(String name, NodeVertice parent, long cost) {
        super(name);
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

    @Override
    public int compareTo(Object o) {
        NodeVertice other = (NodeVertice) o;
        return Long.compare(getCost(), other.getCost());
    }
}
