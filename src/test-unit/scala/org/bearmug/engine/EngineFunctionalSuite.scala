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

  test("scala test unknown destination") {
    val engine = new EngineFunctional(
      Array(new RouteLeg("A", "B", 100)))
    assertResult(Array[NodeVertice]())(engine.route("A", "None"))
  }

/*
  test("scala test two vertices") {
    val engine = new EngineFunctional(
      Array(new RouteLeg("A", "B", 100)))
    assertResult(Array[NodeVertice](
      new NodeVertice("A", 0), new NodeVertice("B", 100)))(engine.route("A", "B"))
  }

  @Test def testTwoVerticesLoop() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "A", 200)))
    assertTrue(Arrays.equals(Array[NodeVertice](new NodeVertice("B", 0), new NodeVertice("A", 200)), engine.route("B", "A")))
  }

  @Test def testTriangle() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("A", "C", 201)))
    assertTrue(Arrays.equals(Array[NodeVertice](new NodeVertice("A", 0), new NodeVertice("B", 100), new NodeVertice("C", 200)), engine.route("A", "C")))
  }

  @Test def testFourVertices() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 1000), new RouteLeg("A", "C", 1000), new RouteLeg("B", "D", 100), new RouteLeg("D", "C", 100), new RouteLeg("A", "D", 1000), new RouteLeg("D", "A", 100)))
    assertTrue(Arrays.equals(Array[NodeVertice](new NodeVertice("A", 0), new NodeVertice("B", 100), new NodeVertice("D", 200), new NodeVertice("C", 300)), engine.route("A", "C")))
  }

  @Test def testChainedShortPath() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("C", "D", 100), new RouteLeg("D", "E", 100), new RouteLeg("E", "F", 100), new RouteLeg("A", "F", 1000)))
    assertTrue(Arrays.equals(Array[NodeVertice](new NodeVertice("A", 0), new NodeVertice("B", 100), new NodeVertice("C", 200), new NodeVertice("D", 300), new NodeVertice("E", 400), new NodeVertice("F", 500)), engine.route("A", "F")))
  }

  @Test def testChainedPathLoop() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("C", "D", 100), new RouteLeg("D", "E", 100), new RouteLeg("E", "F", 100), new RouteLeg("G", "E", 1), new RouteLeg("G", "F", 1), new RouteLeg("A", "F", 1000)))
    assertTrue(Arrays.equals(Array[NodeVertice](new NodeVertice("A", 0), new NodeVertice("B", 100), new NodeVertice("C", 200), new NodeVertice("D", 300), new NodeVertice("E", 400), new NodeVertice("F", 500)), engine.route("A", "F")))
  }

  @Test def testNearbyIsEmpty() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100)))
    assertEquals(0, engine.nearby("source", 100).length)
  }

  @Test def testNearbyEmptyDirection() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100)))
    assertEquals(0, engine.nearby("B", 100).length)
  }

  @Test def testNearbySingle() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100)))
    assertEquals(1, engine.nearby("A", 100).length)
  }

  @Test def testNearbySingleTooFar() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100)))
    assertEquals(0, engine.nearby("A", 99).length)
  }

  @Test def testNearbyTwoPaths() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("C", "D", 100), new RouteLeg("D", "E", 100), new RouteLeg("E", "F", 100), new RouteLeg("A", "F", 1000)))
    assertEquals(5, engine.nearby("A", 500).length)
  }

  @Test def testNearbyTwoPathsReachable() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("C", "D", 100), new RouteLeg("D", "E", 100), new RouteLeg("E", "F", 100), new RouteLeg("A", "F", 1000)))
    assertEquals(5, engine.nearby("A", 1000).length)
  }

  @Test def testNearbyTwoVerticesLoop() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "A", 200)))
    assertEquals(1, engine.nearby("A", 100).length)
    assertEquals(0, engine.nearby("B", 100).length)
  }

  @Test def testNearbyTriangle() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 100), new RouteLeg("A", "C", 201)))
    assertEquals(1, engine.nearby("A", 100).length)
    assertEquals(2, engine.nearby("A", 300).length)
    assertEquals(2, engine.nearby("A", 200).length)
  }

  @Test def testNearbyFourVertices() {
    val engine: EngineFunctional = new EngineFunctional(Array[RouteLeg](new RouteLeg("A", "B", 100), new RouteLeg("B", "C", 1000), new RouteLeg("A", "C", 1000), new RouteLeg("B", "D", 100), new RouteLeg("D", "C", 100), new RouteLeg("A", "D", 1000), new RouteLeg("D", "A", 100)))
    assertEquals(1, engine.nearby("A", 100).length)
    assertEquals(3, engine.nearby("A", 500).length)
    assertEquals(2, engine.nearby("A", 200).length)
    assertEquals(1, engine.nearby("B", 100).length)
  }
  */
}
