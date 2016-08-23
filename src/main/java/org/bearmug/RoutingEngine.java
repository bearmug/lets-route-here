package org.bearmug;

public interface RoutingEngine {

    long route(String source, String destination);

    Route[] nearby(String source, long maxTravelTime);
}
