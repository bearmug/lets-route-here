package org.bearmug;

public class Route {
    private final String source;
    private final String destination;
    private final long travelTime;

    public Route(String source, String destination, long travelTime) {
        this.source = source;
        this.destination = destination;
        this.travelTime = travelTime;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public long getTravelTime() {
        return travelTime;
    }
}
