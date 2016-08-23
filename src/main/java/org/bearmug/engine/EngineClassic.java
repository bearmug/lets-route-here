package org.bearmug.engine;

import org.bearmug.Route;
import org.bearmug.RoutingEngine;

public class EngineClassic implements RoutingEngine {

    public EngineClassic(Route[] edges) {
    }

    @Override
    public long route(String source, String destination) {
        return 0;
    }

    @Override
    public Route[] nearby(String source, long maxTravelTime) {
        return new Route[]{};
    }
}
