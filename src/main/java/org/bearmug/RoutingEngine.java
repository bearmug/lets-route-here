package org.bearmug;

public interface RoutingEngine {

    long route(String source, String destination);

    String[] nearby(String source, long maxTravelTime);
}
