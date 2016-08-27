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

  override def route(source: String, destination: String): Array[NodeVertice] = {

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
    def unwrap(node: NodeVertice, acc: List[NodeVertice]): Array[NodeVertice] =
      if (none.equals(node)) acc.toArray
      else {
        val parent = if (node.getParent == null) none
        else node.getParent
        unwrap(parent, List(new NodeVertice(node.getName, node.getCost)) ++ acc)
      }

    unwrap(
      routeTail(TreeSet(new NodeVertice(source, 0L))(ord), Set()), List())
  }


  override def nearby(source: String, maxTravelTime: Long): Array[String] = {

    @tailrec
    def nearbyTail(stack: List[NodeVertice], acc: Set[String]): Set[String] = {
      if (stack.isEmpty) acc
      else {
        val current = stack.head
        val nestedSet = if (!map.contains(current.getName)) Set.empty[NodeVertice]
        else map(current.getName)
          .filter { l => current.getCost + l.getCost <= maxTravelTime }
          .map { l => new NodeVertice(l.getDest, current.getCost + l.getCost) }
          .toList

        nearbyTail(stack.tail ++ nestedSet, acc + current.getName)
      }
    }

    val srcNode: NodeVertice = new NodeVertice(source, null, 0L)
    (nearbyTail(List(srcNode), Set.empty) - source).toArray[String]
  }
}
