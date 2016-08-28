package org.bearmug.engine;

import org.bearmug.RouteLeg;
import org.bearmug.RoutingEngine;
import org.bearmug.vert.NodeVertice;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EngineClassicTest {

    @Test
    public void testEmptyRouteSet() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{});
        assertTrue(Arrays.equals(new NodeVertice[]{}, engine.route("A", "B")));
    }

    @Test
    public void testAlienSource() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertTrue(Arrays.equals(new NodeVertice[]{}, engine.route("None", "B")));
    }

    @Test
    public void testAlienDestination() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertTrue(Arrays.equals(new NodeVertice[]{}, engine.route("A", "None")));
    }

    @Test
    public void testTwoVertices() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{new NodeVertice("A", 0), new NodeVertice("B", 100)},
                engine.route("A", "B")));
    }

    @Test
    public void testTwoVerticesLoop() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "A", 200)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{new NodeVertice("B", 0), new NodeVertice("A", 200)},
                engine.route("B", "A")));
    }

    @Test
    public void testTriangle() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("A", "C", 201)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{
                        new NodeVertice("A", 0),
                        new NodeVertice("B", 100),
                        new NodeVertice("C", 200)},
                engine.route("A", "C")));
    }

    @Test
    public void testFourVertices() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 1000),
                new RouteLeg("A", "C", 1000),
                new RouteLeg("B", "D", 100),
                new RouteLeg("D", "C", 100),
                new RouteLeg("A", "D", 1000),
                new RouteLeg("D", "A", 100)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{
                        new NodeVertice("A", 0),
                        new NodeVertice("B", 100),
                        new NodeVertice("D", 200),
                        new NodeVertice("C", 300)},
                engine.route("A", "C")));
    }

    @Test
    public void testChainedShortPath() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("C", "D", 100),
                new RouteLeg("D", "E", 100),
                new RouteLeg("E", "F", 100),
                new RouteLeg("A", "F", 1000)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{
                        new NodeVertice("A", 0),
                        new NodeVertice("B", 100),
                        new NodeVertice("C", 200),
                        new NodeVertice("D", 300),
                        new NodeVertice("E", 400),
                        new NodeVertice("F", 500)},
                engine.route("A", "F")));
    }

    @Test
    public void testChainedPathLoop() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("C", "D", 100),
                new RouteLeg("D", "E", 100),
                new RouteLeg("E", "F", 100),
                new RouteLeg("G", "E", 1),
                new RouteLeg("G", "F", 1),
                new RouteLeg("A", "F", 1000)});
        assertTrue(Arrays.equals(
                new NodeVertice[]{
                        new NodeVertice("A", 0),
                        new NodeVertice("B", 100),
                        new NodeVertice("C", 200),
                        new NodeVertice("D", 300),
                        new NodeVertice("E", 400),
                        new NodeVertice("F", 500)},
                engine.route("A", "F")));
    }

    @Test
    public void testTwoBranchesPath() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("C", "D", 100),
                new RouteLeg("A", "AA", 1),
                new RouteLeg("AA", "AAA", 1)});

        assertTrue(Arrays.equals(
                new NodeVertice[]{
                        new NodeVertice("A", 0),
                        new NodeVertice("B", 100),
                        new NodeVertice("C", 200),
                        new NodeVertice("D", 300)},
                engine.route("A", "D")));
    }

    @Test
    public void testNearbyIsEmpty() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertEquals(0, engine.nearby("source", 100).length);
    }

    @Test
    public void testNearbyEmptyDirection() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertEquals(0, engine.nearby("B", 100).length);
    }

    @Test
    public void testNearbySingle() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertEquals(1, engine.nearby("A", 100).length);
    }

    @Test
    public void testNearbySingleTooFar() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{new RouteLeg("A", "B", 100)});
        assertEquals(0, engine.nearby("A", 99).length);
    }

    @Test
    public void testNearbyTwoPaths() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("C", "D", 100),
                new RouteLeg("D", "E", 100),
                new RouteLeg("E", "F", 100),
                new RouteLeg("A", "F", 1000)});
        assertEquals(5, engine.nearby("A", 500).length);
    }

    @Test
    public void testNearbyTwoPathsReachable() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("C", "D", 100),
                new RouteLeg("D", "E", 100),
                new RouteLeg("E", "F", 100),
                new RouteLeg("A", "F", 1000)});
        assertEquals(5, engine.nearby("A", 1000).length);
    }

    @Test
    public void testNearbyTwoVerticesLoop() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "A", 200)});
        assertEquals(1, engine.nearby("A", 100).length);
        assertEquals(0, engine.nearby("B", 100).length);
    }

    @Test
    public void testNearbyTriangle() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 100),
                new RouteLeg("A", "C", 201)});
        assertEquals(1, engine.nearby("A", 100).length);
        assertEquals(2, engine.nearby("A", 300).length);
        assertEquals(2, engine.nearby("A", 200).length);
    }

    @Test
    public void testNearbyFourVertices() {
        RoutingEngine engine = RoutingEngine.classic(new RouteLeg[]{
                new RouteLeg("A", "B", 100),
                new RouteLeg("B", "C", 1000),
                new RouteLeg("A", "C", 1000),
                new RouteLeg("B", "D", 100),
                new RouteLeg("D", "C", 100),
                new RouteLeg("A", "D", 1000),
                new RouteLeg("D", "A", 100)});
        assertEquals(1, engine.nearby("A", 100).length);
        assertEquals(3, engine.nearby("A", 500).length);
        assertEquals(2, engine.nearby("A", 200).length);
        assertEquals(1, engine.nearby("B", 100).length);
    }
}

