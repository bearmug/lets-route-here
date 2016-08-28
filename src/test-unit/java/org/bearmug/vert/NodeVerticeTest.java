package org.bearmug.vert;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class NodeVerticeTest {

    private static final String TEST_STRING = "test-string";

    @Test
    public void testHashCode() {
        NodeVertice v = new NodeVertice(TEST_STRING, 0);
        assertEquals(TEST_STRING.hashCode(), v.hashCode());
    }

    @Test
    public void testEqualsForNull() {
        NodeVertice v = new NodeVertice(TEST_STRING, 0);
        assertFalse(v.equals(null));
    }

    @Test
    public void testEqualsForNonVertice() {
        NodeVertice v = new NodeVertice(TEST_STRING, 0);
        assertFalse(v.equals(new ArrayList<String>()));
    }

    @Test
    public void testEqualsValid() {
        NodeVertice v = new NodeVertice(TEST_STRING, 0);
        NodeVertice another = new NodeVertice(TEST_STRING, 0);
        assertTrue(v.equals(another));
    }

    @Test
    public void testEqualsNodeVertice() {
        NodeVertice v = new NodeVertice(TEST_STRING, 0);
        NodeVertice another = new NodeVertice(TEST_STRING, 1);
        assertTrue(v.equals(another));
    }
}
