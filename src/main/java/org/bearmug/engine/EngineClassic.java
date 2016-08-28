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
    public NodeVertice[] route(String source, String destination) {

        NavigableSet<NodeVertice> set = new TreeSet<>();
        Set<String> visitedNodes = new HashSet<>();
        set.add(new NodeVertice(source, 0));
        while (!set.isEmpty()) {
            // walk through the graph
            NodeVertice current = set.pollFirst();
            visitedNodes.add(current.getName());

            // target node detected
            if (destination.equals(current.getName())) {
                List<NodeVertice> res = new ArrayList<>();
                while (current != null) {
                    res.add(current);
                    current = current.getParent();
                }
                Collections.reverse(res);
                return res.toArray(new NodeVertice[]{});
            }

            // deepen search tree
            List<RouteLeg> legs = directMap.get(current.getName());
            if (legs == null) {
                continue;
            }
            for (RouteLeg leg : legs) {
                if (visitedNodes.contains(leg.getDest())) {
                    continue;
                }
                set.add(new NodeVertice(leg.getDest(), current, current.getCost() + leg.getCost()));
            }
        }
        return new NodeVertice[]{};
    }

    @Override
    public String[] nearby(String source, long maxTravelTime) {
        Stack<NodeVertice> stack = new Stack<>();
        Set<String> res = new HashSet<>();
        stack.push(new NodeVertice(source, 0));
        while (!stack.empty()) {
            NodeVertice current = stack.pop();
            res.add(current.getName());

            List<RouteLeg> legs = directMap.get(current.getName());
            if (legs == null) {
                continue;
            }

            for (RouteLeg leg : legs) {
                if (res.contains(leg.getDest()) || current.getCost() + leg.getCost() > maxTravelTime) {
                    continue;
                }
                stack.add(new NodeVertice(leg.getDest(), current, current.getCost() + leg.getCost()));
            }
        }
        res.remove(source);
        return res.toArray(new String[]{});
    }
}
