package org.bearmug.engine;

import org.bearmug.RoutingEngine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EngineClassicTest {

    @Test
    public void testRouteCostIsZero() {
        RoutingEngine engine = new EngineClassic();
        assertEquals(0, engine.route("source", "destination"));
    }

    @Test
    public void testNearbyIsEmpty() {
        RoutingEngine engine = new EngineClassic();
        assertEquals(0, engine.nearby("source", 100).length);
    }
}
