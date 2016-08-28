package org.bearmug.engine

import org.bearmug.Routing

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scala.math.Ordering

class EnginePureFunc(legs: Array[(String, String, Long)]) extends Routing {

  val map: Map[String, Array[(String, String, Long)]] = legs.groupBy(_._1)

  class Node(val name: String, val cost: Long, val parent: Option[Node]) {}
  val none: Node = new Node("None", 0L, null)
  val ord: Ordering[Node] = Ordering.by(n => n.cost)

  override def route(source: String, destination: String): String = {

    @tailrec
    def routeTail(set: TreeSet[(Node)], visited: Set[String]): Node = {

      if (set.isEmpty) none
      else {
        val current: Node = set.head
        if (destination.equals(current.name)) current
        else {
          val nestedSet = if (!map.contains(current.name)) Set.empty
          else map(current.name)
            .filter { l => !visited.contains(l._2) }
            .map { l => new Node(l._2, current.cost + l._3, Option(current)) }
            .toSet[Node]
          routeTail(set - current ++ nestedSet, visited + current.name)
        }
      }
    }

    @tailrec
    def unwrap(node: Node, acc: List[(String, Long)]): String =
      if (none.equals(node))
        if (acc.isEmpty) "Error: No route from $source to $destination"
        else acc.map(n => n._1 + ":" + n._2).mkString(", ")
      else unwrap(node.parent.getOrElse(none), List((node.name, node.cost)) ++ acc)

    unwrap(
      routeTail(TreeSet(new Node(source, 0L, None))(ord), Set()), List())
  }

  override def nearby(source: String, maxTravelTime: Long): Array[String] = {
    @tailrec
    def nearbyTail(stack: List[(String, Long)], acc: Set[String]): Set[String] = {
      if (stack.isEmpty) acc
      else {
        val current = stack.head
        if (!map.contains(current._1))
          nearbyTail(stack.tail, acc + current._1)
        else {
          val nestedSet = map(current._1)
            .filter { l => current._2 + l._3 <= maxTravelTime }
            .map { l => Tuple2(l._2, current._2 + l._3) }
            .toList

          nearbyTail(stack.tail ++ nestedSet, acc + current._1)
        }
      }
    }

    val srcNode = Tuple2(source, 0L)
    (nearbyTail(List(srcNode), Set.empty) - source).toArray[String]
  }
}
