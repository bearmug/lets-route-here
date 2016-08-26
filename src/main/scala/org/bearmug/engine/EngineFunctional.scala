package org.bearmug.engine

import org.bearmug.vert.NodeVertice
import org.bearmug.{RouteLeg, RoutingEngine}

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scala.math.Ordering

class EngineFunctional(legs: Array[RouteLeg]) extends RoutingEngine {

  val map: Map[String, Array[RouteLeg]] = legs.groupBy(_.getSrc)

  override def route(source: String, destination: String): Array[NodeVertice] = {

    @tailrec
    def routeTail(set: TreeSet[NodeVertice], visited: Set[String], acc: List[NodeVertice]): List[NodeVertice] = {

      if (set.isEmpty) acc
      else {
        val current = set.head
        if (!map.contains(current.getName)) acc
        else if (destination.equals(current.getName)) acc ++ List(current)
        else {
          val nestedSet = map(current.getName)
            .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
            .toSet[NodeVertice]

          routeTail(set - current ++ nestedSet, visited + current.getName, acc ++ List(current))
        }
      }
    }

    val ord = new Ordering[NodeVertice] {
      override def compare(x: NodeVertice, y: NodeVertice): Int = x.compareTo(y)
    }
    routeTail(TreeSet(new NodeVertice(source, null, 0))(ord), Set(),
      List.empty).toArray[NodeVertice]
  }


  override def nearby(source: String, maxTravelTime: Long): Array[String] = {

    @tailrec
    def nearbyTail(stack: List[NodeVertice], acc: Set[String]): Set[String] = {
      if (stack.isEmpty) acc
      else {
        val current = stack.head
        if (!map.contains(current.getName)) acc
        else {
          val nestedSet = map(current.getName)
            .filter { l => current.getCost + l.getCost < maxTravelTime }
            .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
            .toList

          nearbyTail(stack.tail ++ nestedSet, acc + current.getName)
        }
      }
    }

    nearbyTail(List(new NodeVertice(source, null, 0)), Set.empty).toArray[String]
  }
}
