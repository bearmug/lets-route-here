package org.bearmug.engine;

import org.bearmug.RouteLeg;
import org.bearmug.RoutingEngine;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EngineClassic implements RoutingEngine {

    private final Set<String> vertices;

    private final Queue<RouteLeg> routeQueue = new PriorityQueue<>(
            Comparator.comparingLong(RouteLeg::getTravelTime));

    public EngineClassic(RouteLeg[] edges) {
        vertices = Collections.unmodifiableSet(
                Arrays.stream(edges)
                        .flatMap(route -> Stream.of(route.getSource(), route.getDestination()))
                        .collect(Collectors.toSet()));
        routeQueue.addAll(Arrays.asList(edges));
    }

    @Override
    public long route(String source, String destination) {
        return 0;
    }

    @Override
    public RouteLeg[] nearby(String source, long maxTravelTime) {
        return new RouteLeg[]{};
    }
}
