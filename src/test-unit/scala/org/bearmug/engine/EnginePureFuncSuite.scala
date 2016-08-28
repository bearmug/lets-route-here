package org.bearmug.engine

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EnginePureFuncSuite extends FunSuite {

  test("engine route cost is 0") {
    val engine = new EnginePureFunc(Array())
    engine.route("source", "destination") == "Error: No route from source to destination"
  }

  test("engine tell that nearby is nothing") {
    val engine = new EnginePureFunc(Array())
    assertResult(Array())(engine.nearby("source", 100))
  }

  test("ignore unknown destination") {
    val engine = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    engine.route("A", "None") == "Error: No route from A to None"
  }

  test("route two vertices") {
    val engine = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    engine.route("A", "B") == "A -> B : 100"
  }

  test("route through two vertices loop") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "A", 200)))

    engine.route("B", "A") == "B -> A : 200"
  }

  test("build triangle route") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("A", "C", 201)))

    engine.route("A", "C") == "A -> B -> C : 200"
  }

  test("build four vertices route") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 1000),
      Tuple3("A", "C", 1000),
      Tuple3("B", "D", 100),
      Tuple3("D", "C", 100),
      Tuple3("A", "D", 1000),
      Tuple3("D", "A", 100)))

    engine.route("A", "C") == "A -> B -> D -> C : 300"
  }

  test("detect chained shortest path") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("C", "D", 100),
      Tuple3("D", "E", 100),
      Tuple3("E", "F", 100),
      Tuple3("A", "F", 1000)))

    engine.route("A", "F") == "A -> B -> C -> D -> E -> F : 500"
  }

  test("chained path with loop") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("C", "D", 100),
      Tuple3("D", "E", 100),
      Tuple3("E", "F", 100),
      Tuple3("G", "E", 1),
      Tuple3("G", "F", 1),
      Tuple3("A", "F", 1000)))

    engine.route("A", "F") == "A -> B -> C -> D -> E -> F : 500"
  }

  test("two branches path") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("C", "D", 100),
      Tuple3("A", "AA", 1),
      Tuple3("AA", "AAA", 1)))

    engine.route("A", "D") == "A -> B -> C -> D : 300"
  }

  test("nearby is empty") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    assertResult(0)(engine.nearby("source", 100).length)
  }

  test("nearby for empty direction") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("nearby single node") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    assertResult(1)(engine.nearby("A", 100).length)
  }

  test("nearby single node too far") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100)))

    assertResult(0)(engine.nearby("A", 99).length)
  }

  test("nearby for two paths") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("C", "D", 100),
      Tuple3("D", "E", 100),
      Tuple3("E", "F", 100),
      Tuple3("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 500).length)
  }

  test("nearby for alternative paths") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("C", "D", 100),
      Tuple3("D", "E", 100),
      Tuple3("E", "F", 100),
      Tuple3("A", "F", 1000)))

    assertResult(5)(engine.nearby("A", 1000).length)
  }

  test("nearby for cycled two vertices") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "A", 200)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(0)(engine.nearby("B", 100).length)
  }

  test("nearby with triangle") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 100),
      Tuple3("A", "C", 201)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(2)(engine.nearby("A", 300).length)
    assertResult(2)(engine.nearby("A", 200).length)
  }

  test("nearby four vertices") {
    val engine: EnginePureFunc = new EnginePureFunc(Array(
      Tuple3("A", "B", 100),
      Tuple3("B", "C", 1000),
      Tuple3("A", "C", 1000),
      Tuple3("B", "D", 100),
      Tuple3("D", "C", 100),
      Tuple3("A", "D", 1000),
      Tuple3("D", "A", 100)))

    assertResult(1)(engine.nearby("A", 100).length)
    assertResult(3)(engine.nearby("A", 500).length)
    assertResult(2)(engine.nearby("A", 200).length)
    assertResult(1)(engine.nearby("B", 100).length)
  }
}
