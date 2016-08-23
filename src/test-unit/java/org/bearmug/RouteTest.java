package org.bearmug;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RouteTest {

    private static final String SOURCE = "src";
    private static final String DESTINATION = "dst";
    private static final long TIME = 1000;

    @Test
    public void testRouteStoresParameters() {
        Route route = new Route(SOURCE, DESTINATION, TIME);
        assertEquals(SOURCE, route.getSource());
        assertEquals(DESTINATION, route.getDestination());
        assertEquals(TIME, route.getTravelTime());
    }
}
