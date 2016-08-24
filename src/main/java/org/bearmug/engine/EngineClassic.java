package org.bearmug.engine;

import org.bearmug.RouteLeg;
import org.bearmug.RoutingEngine;
import org.bearmug.vert.NodeVertice;
import org.bearmug.vert.RouteVertice;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Java {@link RoutingEngine} version. Avoiding recursive implementations here for
 * performance/call stack safety purposes.
 */
public class EngineClassic implements RoutingEngine {

    private final Map<String, RouteVertice> reversedMap = new HashMap<>();
    private final Map<String, RouteVertice> directMap = new HashMap<>();

    public EngineClassic(RouteLeg[] legs) {
        Arrays.stream(legs)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(RouteLeg::getDest), Function.identity()))
                .entrySet()
                .forEach(e -> {
                    reversedMap.put(e.getKey(), new RouteVertice(e.getKey(), e.getValue(), true));
                });
        Arrays.stream(legs)
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(RouteLeg::getSrc), Function.identity()))
                .entrySet()
                .forEach(e -> {
                    directMap.put(e.getKey(), new RouteVertice(e.getKey(), e.getValue()));
                });
    }

    /**
     * Reversed graph traversal Dijkstra approach used to find shortest path. Direct approach avoided to prevent
     * extra data structures population at beginning of each iteration.
     *
     * @param source      source node to find route from
     * @param destination dest node to find route to
     * @return route cost
     */
    @Override
    public long route(String source, String destination) {

        NavigableSet<NodeVertice> set = new TreeSet<>();
        Set<String> visitedNodes = new HashSet<>();
        set.add(new NodeVertice(destination, 0));
        while (!set.isEmpty()) {
            // walk through the graph
            NodeVertice current = set.pollFirst();
            visitedNodes.add(current.getName());

            // target node detected
            if (source.equals(current.getName())) {
                return current.getCost();
            }

            // deepen search tree
            for (Map.Entry<String, Long> e : reversedMap.get(current.getName()).getDirectPeers().entrySet()) {
                if (visitedNodes.contains(e.getKey())) {
                    continue;
                }
                set.add(new NodeVertice(e.getKey(), current.getCost() + e.getValue()));
            }
        }
        return -1;
    }

    @Override
    public RouteLeg[] nearby(String source, long maxTravelTime) {
        return new RouteLeg[]{};
    }
}
