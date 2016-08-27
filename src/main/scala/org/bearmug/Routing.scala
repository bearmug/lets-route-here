package org.bearmug

trait Routing {
  def route(source: String, destination: String): Array[(String, Long)]

  def nearby(source: String, maxTravelTime: Long): Array[String]
}
