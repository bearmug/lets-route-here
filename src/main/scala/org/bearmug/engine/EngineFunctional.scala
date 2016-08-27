package org.bearmug.engine

import org.bearmug.vert.NodeVertice
import org.bearmug.{RouteLeg, RoutingEngine}

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scala.math.Ordering

class EngineFunctional(legs: Array[RouteLeg]) extends RoutingEngine {

  val map: Map[String, Array[RouteLeg]] = legs.groupBy(_.getSrc)

  val ord = new Ordering[NodeVertice] {
    override def compare(x: NodeVertice, y: NodeVertice): Int = x.compareTo(y)
  }

  override def route(source: String, destination: String): Array[NodeVertice] = {

    @tailrec
    def routeTail(set: TreeSet[NodeVertice], visited: Set[String], acc: List[NodeVertice]): List[NodeVertice] = {

      if (set.isEmpty) List.empty
      else {
        val current = set.head
        if (destination.equals(current.getName)) acc ++ List(current)
        else {
          val nestedSet = if (!map.contains(current.getName)) Set.empty[NodeVertice]
          else map(current.getName)
            .filter(l => !visited.contains(l.getDest))
            .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
            .toSet[NodeVertice]
          routeTail(set - current ++ nestedSet, visited + current.getName, acc ++ List(current))
        }
      }
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
        if (!map.contains(current.getName))
          nearbyTail(stack.tail, acc + current.getName)
        else {
          val nestedSet = map(current.getName)
            .filter { l => current.getCost + l.getCost <= maxTravelTime }
            .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
            .toList

          nearbyTail(stack.tail ++ nestedSet, acc + current.getName)
        }
      }
    }

    val srcNode: NodeVertice = new NodeVertice(source, null, 0)
    (nearbyTail(List(srcNode), Set.empty) - source).toArray[String]
  }
}
