package org.bearmug.engine

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EnginePureFunctionalTest extends FunSuite {

  test("stub route test") {
    val engine = new EnginePureFunctional(Array())
    assertResult(Array())(engine.route("source", "destination"))
  }

  test("stub nearby test") {
    val engine = new EnginePureFunctional(Array())
    assertResult(0)(engine.nearby("source", 100).length)
  }
}
