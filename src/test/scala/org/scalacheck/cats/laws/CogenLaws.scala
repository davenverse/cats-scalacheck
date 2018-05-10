package org.scalacheck.cats.laws

import cats.laws.discipline._
import cats.tests.CatsSuite
import org.scalacheck.Cogen
import org.scalacheck.cats.ScalaCheckSetup
import org.scalacheck.cats.instances.CogenInstances._

class CogenLaws extends CatsSuite with ScalaCheckSetup {
  checkAll("Cogen", ContravariantSemigroupalTests[Cogen].contravariant[Int, Int, Int])
  checkAll("Cogen", MonoidKTests[Cogen].monoidK[Int])
}