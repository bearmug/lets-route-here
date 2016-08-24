package org.bearmug;

import org.bearmug.vert.NodeVertice;

public interface RoutingEngine {

    NodeVertice[] route(String source, String destination);

    String[] nearby(String source, long maxTravelTime);
}
