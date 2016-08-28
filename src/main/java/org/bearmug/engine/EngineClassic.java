package org.bearmug.engine;

import org.bearmug.RouteLeg;
import org.bearmug.RoutingEngine;
import org.bearmug.vert.NodeVertice;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Java {@link RoutingEngine} version. Avoiding recursive implementations here for
 * performance/call stack safety purposes.
 */
public class EngineClassic implements RoutingEngine {

    private final Map<String, List<RouteLeg>> directMap;

    public EngineClassic(RouteLeg[] legs) {
        directMap = Arrays.stream(legs)
                .collect(Collectors.groupingBy(RouteLeg::getSrc));
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
    public String route(String source, String destination) {
        return process(source, Converter.routeConverter(source, destination));
    }

    @Override
    public String nearby(String source, long maxTravelTime) {
        return process(source, Converter.nearbyConverter(source, maxTravelTime));
    }

    private String process(String source, Converter c) {
        NavigableSet<NodeVertice> set = new TreeSet<>();
        Set<NodeVertice> visitedNodes = new HashSet<>();
        set.add(new NodeVertice(source, 0));
        while (!set.isEmpty()) {
            // walk through the graph
            NodeVertice current = set.pollFirst();
            visitedNodes.add(current);

            // target node detected
            if (c.enough(current)) {
                return c.result(current, visitedNodes);
            }

            // deepen search tree
            List<RouteLeg> legs = directMap.get(current.getName());
            if (legs == null) {
                continue;
            }
            for (RouteLeg leg : legs) {
                NodeVertice vertice = new NodeVertice(leg.getDest(), current, current.getCost() + leg.getCost());
                if (c.shouldContinue(vertice, visitedNodes)) {
                    continue;
                }
                set.add(vertice);
            }
        }
        visitedNodes.remove(new NodeVertice(source, null, 0));
        return c.complete(visitedNodes);
    }
}
