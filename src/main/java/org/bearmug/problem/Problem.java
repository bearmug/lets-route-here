package org.bearmug.problem;

import org.bearmug.RoutingEngine;

public interface Problem extends FunctionalInterface {


    boolean solveAndPrint(RoutingEngine engine);
}
