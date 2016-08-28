package org.bearmug

trait Routing {
  def route(source: String, destination: String): String

  def nearby(source: String, maxTravelTime: Long): Array[String]
}
