package org.bearmug.engine

import org.bearmug.Routing

class EnginePureFunctional(legs: Array[(String, String, Long)]) extends Routing {

  override def route(source: String, destination: String): Array[(String, Long)] =
    Array()

  override def nearby(source: String, maxTravelTime: Long): Array[String] =
    Array()
}
