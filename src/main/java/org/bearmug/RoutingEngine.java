package org.bearmug;

import org.bearmug.engine.EngineClassic;
import org.bearmug.engine.EngineInteropFunc;
import org.bearmug.vert.NodeVertice;

public interface RoutingEngine {

    static RoutingEngine engineClassic(RouteLeg[] legs) {
        return new EngineClassic(legs);
    }

    static RoutingEngine engineInterop(RouteLeg[] legs) {
        return new EngineInteropFunc(legs);
    }

    NodeVertice[] route(String source, String destination);

    String[] nearby(String source, long maxTravelTime);
}
