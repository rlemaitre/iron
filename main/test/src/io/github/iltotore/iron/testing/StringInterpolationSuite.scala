package io.github.iltotore.iron.testing

import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.string.*
import io.github.iltotore.iron.macros.StringLiteral
import utest.*

object StringInterpolationSuite extends TestSuite:

  extension (inline sc: StringContext)
    inline def v(inline args: Any*): Version = StringLiteral(sc, args)

  val tests: Tests = Tests {
    test("StringInterpolation") {
      val major = 2
      val minor = 4
      val patch = 0
      test - assert(Version("2.4.0") == v"2.4.0")
      test - assert(Version("2.4.0") == v"$major.$minor.$patch")
    }
  }
