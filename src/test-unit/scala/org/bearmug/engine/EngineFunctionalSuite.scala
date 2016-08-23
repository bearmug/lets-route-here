package org.bearmug.engine

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EngineFunctionalSuite extends FunSuite {

  test("scala engine route cost is 0") {
    def engine = new EngineFunctional()
    assertResult(0)(engine.route("source", "destination"))
  }

  test("scala engine tell that nearby is nothing") {
    def engine = new EngineFunctional()
    assertResult(Array())(engine.nearby("source", 100))
  }
}
