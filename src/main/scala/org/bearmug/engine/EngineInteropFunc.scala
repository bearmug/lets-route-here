package org.bearmug.engine

import org.bearmug.vert.NodeVertice
import org.bearmug.{RouteLeg, RoutingEngine}

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scala.math.Ordering

class EngineInteropFunc(legs: Array[RouteLeg]) extends RoutingEngine {

  val map: Map[String, Array[RouteLeg]] = legs.groupBy(_.getSrc)

  val none: NodeVertice = new NodeVertice("", 0)
  val ord: Ordering[NodeVertice] = Ordering.by(v => v.getCost)

  override def route(source: String, destination: String): String = {

    def complete(n: NodeVertice) =
      destination.eq(n.getName)

    @tailrec
    def routeTail(set: TreeSet[NodeVertice], visited: Set[String]): NodeVertice = {

      if (set.isEmpty) none
      else {
        val current = set.head
        if (destination.equals(current.getName)) current
        else {
          val nestedSet = if (!map.contains(current.getName)) Set.empty[NodeVertice]
          else map(current.getName)
            .filter(l => !visited.contains(l.getDest))
            .map { l => new NodeVertice(l.getDest, current, current.getCost + l.getCost) }
            .toSet[NodeVertice]
          routeTail(set - current ++ nestedSet, visited + current.getName)
        }
      }
    }

    @tailrec
    def unwrap(node: NodeVertice, acc: List[NodeVertice]): String =
      if (none.equals(node))
        if (acc.isEmpty) "Error: No route from $source to $destination"
        else acc.map(n => n.getName + ":" + n.getCost).mkString(", ")
      else {
        val parent = if (node.getParent == null) none
        else node.getParent
        unwrap(parent, List(new NodeVertice(node.getName, node.getCost)) ++ acc)
      }

    unwrap(
      routeTail(TreeSet(new NodeVertice(source, 0L))(ord), Set()), List())
  }


  override def nearby(source: String, maxTravelTime: Long): String = {

    @tailrec
    def nearbyTail(stack: List[NodeVertice], acc: Set[NodeVertice]): String = {
      if (stack.isEmpty)
        if (acc.isEmpty) s"Error: Nothing nearby $source within $maxTravelTime range"
        else acc
          .map(n => n.getName + ": " + n.getCost)
          .mkString(", ")
      else {
        val current = stack.head
        val nestedSet = if (!map.contains(current.getName)) Set.empty[NodeVertice]
        else map(current.getName)
          .filter { l => current.getCost + l.getCost <= maxTravelTime }
          .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
          .toList

        nearbyTail(stack.tail ++ nestedSet, acc + current)
      }
    }

    val srcNode: NodeVertice = new NodeVertice(source, null, 0L)
    nearbyTail(List(srcNode), TreeSet()(ord))
  }
}
