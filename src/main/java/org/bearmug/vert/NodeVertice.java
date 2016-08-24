package org.bearmug.vert;

public class NodeVertice extends Vertice implements Comparable {

    final long cost;

    public NodeVertice(String name, long cost) {
        super(name);
        this.cost = cost;
    }

    public long getCost() {
        return cost;
    }

    @Override
    public int compareTo(Object o) {
        NodeVertice other = (NodeVertice) o;
        return Long.compare(getCost(), other.getCost());
    }
}
