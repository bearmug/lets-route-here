package org.bearmug.engine

import org.bearmug.RoutingEngine
import org.bearmug.vert.NodeVertice

class EngineFunctional extends RoutingEngine {
  override def route(source: String, destination: String): Array[NodeVertice] = Array()

  override def nearby(source: String, maxTravelTime: Long): Array[String] = Array()
}
