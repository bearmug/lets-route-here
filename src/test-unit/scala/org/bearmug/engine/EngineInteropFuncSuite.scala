package org.bearmug.engine

import org.bearmug.RouteLeg
import org.bearmug.vert.NodeVertice
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EngineInteropFuncSuite extends FunSuite {

  test("engine route cost is 0") {
    val engine = new EngineInteropFunc(Array())
    assertResult(Array())(engine.route("source", "destination"))
  }

  test("engine tell that nearby is nothing") {
    val engine = new EngineInteropFunc(Array())
    assertResult(Array())(engine.nearby("source", 100))
  }

  test("ignore unknown destination") {
    val engine = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(Array[NodeVertice]())(engine.route("A", "None"))
  }


  test("route two vertices") {
    val engine = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(Array[NodeVertice](
      new NodeVertice("A", 0),
      new NodeVertice("B", 100))
    )(engine.route("A", "B"))
  }

  test("route through two vertices loop") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "A", 200)))

    assertResult(Array(
      new NodeVertice("B", 0),
      new NodeVertice("A", 200))
    )(engine.route("B", "A"))
  }

  test("build triangle route") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("A", "C", 201)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("C", 200))
    )(engine.route("A", "C"))
  }

  test("build four vertices route") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
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

  test("detect chained shortest path") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
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

  test("chained path with loop") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
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

  test("two branches path") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("A", "AA", 1),
      new RouteLeg("AA", "AAA", 1)))

    assertResult(Array(
      new NodeVertice("A", 0),
      new NodeVertice("B", 100),
      new NodeVertice("C", 200),
      new NodeVertice("D", 300))
    )(engine.route("A", "D"))
  }

  test("nearby is empty") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("source", 100).length)
  }

  test("nearby for empty direction") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("nearby single node") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(1)(engine.nearby("A", 100).length)
  }

  test("nearby single node too far") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100)))

    assertResult(0)(engine.nearby("A", 99).length)
  }

  test("nearby for two paths") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 500).length)
  }

  test("nearby for alternative paths") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("C", "D", 100),
      new RouteLeg("D", "E", 100),
      new RouteLeg("E", "F", 100),
      new RouteLeg("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 1000).length)
  }

  test("nearby for cycled two vertices") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "A", 200)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("nearby with triangle") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
      new RouteLeg("A", "B", 100),
      new RouteLeg("B", "C", 100),
      new RouteLeg("A", "C", 201)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(2)(engine.nearby("A", 300).length)
    assertResult(2)(engine.nearby("A", 200).length)
  }

  test("nearby four vertices") {
    val engine: EngineInteropFunc = new EngineInteropFunc(Array(
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
