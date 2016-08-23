package org.bearmug.engine;

import org.bearmug.Route;
import org.bearmug.RoutingEngine;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EngineClassicTest {

    @Test
    public void testTwoVertices() {
        RoutingEngine engine = new EngineClassic(new Route[]{new Route("A", "B", 100)});
        assertEquals(100, engine.route("A", "B"));
    }

    @Test
    public void testTwoVerticesLoop() {
        RoutingEngine engine = new EngineClassic(new Route[]{
                new Route("A", "B", 100),
                new Route("B", "A", 200)});
        assertEquals(200, engine.route("B", "A"));
    }

    @Test
    public void testTriangle() {
        RoutingEngine engine = new EngineClassic(new Route[]{
                new Route("A", "B", 100),
                new Route("B", "C", 100),
                new Route("A", "C", 201)});
        assertEquals(200, engine.route("A", "C"));
    }

    @Test
    public void testFourVertices() {
        RoutingEngine engine = new EngineClassic(new Route[]{
                new Route("A", "B", 100),
                new Route("B", "C", 1000),
                new Route("A", "C", 1000),
                new Route("B", "D", 100),
                new Route("D", "C", 100),
                new Route("A", "D", 1000),
                new Route("D", "A", 100)});
        assertEquals(300, engine.route("A", "C"));
    }

    @Test
    public void testNearbyIsEmpty() {
        RoutingEngine engine = new EngineClassic(new Route[]{new Route("A", "B", 100)});
        assertEquals(0, engine.nearby("source", 100).length);
    }
}
