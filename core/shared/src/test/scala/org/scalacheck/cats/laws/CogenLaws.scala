package org.scalacheck.cats.laws

import cats.laws.discipline._
import org.scalacheck.Cogen
import org.scalacheck.cats.ScalaCheckSetup
import org.scalacheck.cats.instances.CogenInstances._

class CogenLaws extends munit.DisciplineSuite with ScalaCheckSetup {
  checkAll("Cogen", ContravariantSemigroupalTests[Cogen].contravariant[Int, Int, Int])
  checkAll("Cogen", MonoidKTests[Cogen].monoidK[Int])
}