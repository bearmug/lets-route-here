package org.bearmug;

import org.bearmug.engine.EngineClassic;
import org.bearmug.engine.EngineInteropFunc;

public interface RoutingEngine {

    static RoutingEngine classic(RouteLeg[] legs) {
        return new EngineClassic(legs);
    }

    static RoutingEngine interop(RouteLeg[] legs) {
        return new EngineInteropFunc(legs);
    }

    String route(String source, String destination);

    String[] nearby(String source, long maxTravelTime);
}
