package org.bearmug.engine

import org.bearmug.{RouteLeg, RoutingEngine}

class EngineFunctional extends RoutingEngine {
  override def route(source: String, destination: String): Long = 0

  override def nearby(source: String, maxTravelTime: Long): Array[RouteLeg] =
    Array()
}
