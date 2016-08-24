package org.bearmug.vert;

import org.bearmug.RouteLeg;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class RouteVertice extends Vertice {
    private final Map<String, Long> directPeers;

    public RouteVertice(String name, Collection<RouteLeg> peers) {
        this(name, peers, false);
    }

    public RouteVertice(String name, Collection<RouteLeg> peers, boolean reversed) {
        super(name);
        this.directPeers = Collections.unmodifiableMap(
                peers.stream().collect(Collectors.toMap(
                        reversed ? RouteLeg::getSrc : RouteLeg::getDest,
                        RouteLeg::getCost)));
    }

    public Map<String, Long> getDirectPeers() {
        return directPeers;
    }
}
