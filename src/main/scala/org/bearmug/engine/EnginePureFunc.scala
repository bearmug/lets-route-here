package org.bearmug.engine

import org.bearmug.Routing

import scala.annotation.tailrec
import scala.collection.immutable.TreeSet

class EnginePureFunc(legs: Array[(String, String, Long)]) extends Routing {

  val map: Map[String, Array[(String, String, Long)]] = legs.groupBy(_._1)

  override def route(source: String, destination: String): Array[(String, Long)] = {

    @tailrec
    def routeTail(set: TreeSet[(String, Long)], visited: Set[String], acc: List[(String, Long)]): List[(String, Long)] = {

      if (set.isEmpty) List.empty
      else {
        val current = set.head
        if (destination.equals(current._1)) acc ++ List(current)
        else {
          val nestedSet = if (!map.contains(current._1)) Set.empty
          else map(current._1)
            .filter(l => !visited.contains(l._1))
            .map { l => Tuple2(l._1, current._2 + l._3) }
            .toSet[(String, Long)]
          routeTail(set - current ++ nestedSet, visited + current._1, acc ++ List(current))
        }
      }
    }

    routeTail(TreeSet(Tuple2(source, 0)), Set(), List.empty)
      .toArray[(String, Long)]
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
