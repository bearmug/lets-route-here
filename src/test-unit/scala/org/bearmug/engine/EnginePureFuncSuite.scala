package org.bearmug.engine

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EnginePureFuncSuite extends FunSuite {

  test("stub route test") {
    val engine = new EnginePureFunc(Array())
    assertResult(Array())(engine.route("source", "destination"))
  }

  test("stub nearby test") {
    val engine = new EnginePureFunc(Array())
    assertResult(0)(engine.nearby("source", 100).length)
  }
}
