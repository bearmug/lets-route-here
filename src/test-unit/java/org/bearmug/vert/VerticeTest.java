package org.bearmug.vert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class VerticeTest {

    private static final String TEST_STRING = "test-string";

    @Test
    public void testHashCode() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        assertEquals(TEST_STRING.hashCode(), v.hashCode());
    }

    @Test
    public void testEqualsForNull() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        assertFalse(v.equals(null));
    }

    @Test
    public void testEqualsForNonVertice() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        assertFalse(v.equals(new ArrayList<String>()));
    }

    @Test
    public void testEqualsValid() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        Vertice another = new Vertice(TEST_STRING) {
        };
        assertTrue(v.equals(another));
    }

    @Test
    public void testEqualsRouteVertice() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        Vertice another = new RouteVertice(TEST_STRING, Collections.emptyList());
        assertTrue(v.equals(another));
    }

    @Test
    public void testEqualsNodeVertice() {
        Vertice v = new Vertice(TEST_STRING) {
        };
        Vertice another = new NodeVertice(TEST_STRING, 0);
        assertTrue(v.equals(another));
    }
}
