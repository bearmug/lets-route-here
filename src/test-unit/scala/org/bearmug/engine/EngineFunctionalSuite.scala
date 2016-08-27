package org.bearmug.engine

import org.bearmug.RouteLeg
import org.bearmug.vert.NodeVertice
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EngineFunctionalSuite extends FunSuite {

  test("scala engine route cost is 0") {
    val engine = new EngineFunctional(Array())
    assertResult(Array())(engine.route("source", "destination"))
  }

  test("scala engine tell that nearby is nothing") {
    val engine = new EngineFunctional(Array())
    assertResult(Array())(engine.nearby("source", 100))
  }

  test("scala ignore unknown destination") {
    val engine = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(Array[NodeVertice]())(engine.route("A", "None"))
  }


  test("scala route two vertices") {
    val engine = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(Array[NodeVertice](
      new NodeVertice("A", 0),
      new NodeVertice("B", 100))
    )(engine.route("A", "B"))
  }

  test("scala route through two vertices loop") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "A", 200)))

    assertResult(Array(
      new NodeVertice("B", 0),
      new NodeVertice("A", 200))
    )(engine.route("B", "A"))
  }

  test("scala build triangle route") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("A", "C", 201)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("C", 200))
    )(engine.route("A", "C"))
  }

  test("scala build four vertices route") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 1000),
      new RouteLeg("A", "C", 1000),
      new RouteLeg("B", "D", 100),
      new RouteLeg("D", "C", 100),
      new RouteLeg("A", "D", 1000),
      new RouteLeg("D", "A", 100)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("D", 200),
      new NodeVertice("C", 300))
    )(engine.route("A", "C"))
  }

  test("scala detect chained shortest path") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("A", "F", 1000)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("C", 200),
      new NodeVertice("D", 300),
      new NodeVertice("E", 400),
      new NodeVertice("F", 500))
    )(engine.route("A", "F"))
  }

  test("scala chained path with loop") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("G", "E", 1),
      new RouteLeg("G", "F", 1),
      new RouteLeg("A", "F", 1000)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("C", 200),
      new NodeVertice("D", 300),
      new NodeVertice("E", 400),
      new NodeVertice("F", 500))
    )(engine.route("A", "F"))
  }

  test("scala nearby is empty") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("source", 100).length)
  }

  test("NearbyEmptyDirection") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("NearbySingle") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(1)(engine.nearby("A", 100).length)
  }

  test("NearbySingleTooFar") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("A", 99).length)
  }

  test("NearbyTwoPaths") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 500).length)
  }

  test("NearbyTwoPathsReachable") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 1000).length)
  }

  test("NearbyTwoVerticesLoop") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "A", 200)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("NearbyTriangle") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("A", "C", 201)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(2)(engine.nearby("A", 300).length)
    assertResult(2)(engine.nearby("A", 200).length)
  }

  test("NearbyFourVertices") {
    val engine: EngineFunctional = new EngineFunctional(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 1000),
      new RouteLeg("A", "C", 1000),
      new RouteLeg("B", "D", 100),
      new RouteLeg("D", "C", 100),
      new RouteLeg("A", "D", 1000),
      new RouteLeg("D", "A", 100)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(3)(engine.nearby("A", 500).length)
    assertResult(2)(engine.nearby("A", 200).length)
    assertResult(1)(engine.nearby("B", 100).length)
  }
}
