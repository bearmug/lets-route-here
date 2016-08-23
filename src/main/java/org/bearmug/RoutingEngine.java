package org.bearmug;

public interface RoutingEngine {

    long route(String source, String destination);

    RouteLeg[] nearby(String source, long maxTravelTime);
}
