package org.bearmug.engine

import org.bearmug.vert.NodeVertice
import org.bearmug.{RouteLeg, RoutingEngine}

import scala.collection.immutable.TreeSet
import scala.math.Ordering

class EngineFunctional(legs: Array[RouteLeg]) extends RoutingEngine {

  val map: Map[String, Array[RouteLeg]] = legs.groupBy(_.getSrc)

  override def route(source: String, destination: String): Array[NodeVertice] = {
    def routeRec(set: TreeSet[NodeVertice], visitedNodes: Set[String]): List[NodeVertice] = {

      if (set.isEmpty) List.empty[NodeVertice]
      else {
        val current = set.head
        if (destination.equals(current.getName)) List(current)
        else if (!map.contains(current.getName)) List(current)
        else {
          val nestedSet = map(current.getName)
            .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
            .toSet[NodeVertice]

          List(current) ++ routeRec(set - current ++ nestedSet, visitedNodes + current.getName)
        }
      }
    }

    val ord = new Ordering[NodeVertice] {
      override def compare(x: NodeVertice, y: NodeVertice): Int = x.compareTo(y)
    }
    routeRec(TreeSet.empty[NodeVertice](ord), Set()).toArray[NodeVertice]
  }


  override def nearby(source: String, maxTravelTime: Long): Array[String] = Array()
}
