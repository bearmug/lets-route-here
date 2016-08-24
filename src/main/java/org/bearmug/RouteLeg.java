package org.bearmug;

public class RouteLeg {
    private final String src;
    private final String dest;
    private final long cost;

    public RouteLeg(String src, String dest, long cost) {
        this.src = src;
        this.dest = dest;
        this.cost = cost;
    }

    public String getSrc() {
        return src;
    }

    public String getDest() {
        return dest;
    }

    public long getCost() {
        return cost;
    }
}
