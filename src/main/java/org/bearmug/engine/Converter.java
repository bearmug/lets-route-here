package org.bearmug.engine;

import org.bearmug.vert.NodeVertice;

import java.util.*;
import java.util.stream.Collectors;

public interface Converter {

    String ERROR_NO_ROUTE = "Error: No route from %s to %s";
    String ERROR_NO_NEARBY = "Error: Nothing nearby %s within %d range";

    static Converter routeConverter(String source, String destination) {
        return new RouteConverter(source, destination);
    }

    static Converter nearbyConverter(String source, long maxTravelTime) {
        return new NearbyConverter(source, maxTravelTime);
    }

    boolean enough(NodeVertice vertice);

    String result(NodeVertice current, Set<NodeVertice> visited);

    String complete(Set<NodeVertice> visited);

    boolean shouldContinue(NodeVertice vertice, Set<NodeVertice> visitedNodes);

    class RouteConverter implements Converter {

        private final String destination;
        private final String source;

        RouteConverter(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }

        @Override
        public boolean enough(NodeVertice vertice) {
            return destination.equals(vertice.getName());
        }

        @Override
        public String result(NodeVertice current, Set<NodeVertice> visited) {
            List<NodeVertice> res = new ArrayList<>();
            long cost = current.getCost();
            while (current != null) {
                res.add(current);
                current = current.getParent();
            }
            Collections.reverse(res);
            return res.stream()
                    .map(NodeVertice::getName)
                    .collect(Collectors.joining(" -> ")) + " : " + cost;
        }

        @Override
        public String complete(Set<NodeVertice> visited) {
            return String.format(ERROR_NO_ROUTE, source, destination);
        }

        @Override
        public boolean shouldContinue(NodeVertice vertice, Set<NodeVertice> visitedNodes) {
            return visitedNodes.contains(vertice);
        }
    }

    class NearbyConverter implements Converter {

        private final String source;
        private final long maxTravelTime;

        public NearbyConverter(String source, long maxTravelTime) {
            this.source = source;
            this.maxTravelTime = maxTravelTime;
        }

        @Override
        public boolean enough(NodeVertice vertice) {
            return vertice.getCost() > maxTravelTime;
        }

        @Override
        public String result(NodeVertice current, Set<NodeVertice> visited) {
            return new TreeSet<>(visited).stream()
                    .filter(n -> !n.getName().equals(source))
                    .map(n -> n.getName() + ": " + n.getCost())
                    .collect(Collectors.joining(", "));
        }

        @Override
        public String complete(Set<NodeVertice> visited) {
            if (!visited.isEmpty()) {
                return result(null, visited);
            }
            return String.format(ERROR_NO_NEARBY, source, maxTravelTime);
        }

        @Override
        public boolean shouldContinue(NodeVertice vertice, Set<NodeVertice> visitedNodes) {
            return visitedNodes.contains(vertice) || vertice.getCost() > maxTravelTime;
        }
    }
}
