package org.scalacheck.cats.laws


import cats.laws.discipline._
import cats.kernel.laws.discipline._
import cats.data.NonEmptyList
import cats.tests.CatsSuite
import org.scalacheck._
import org.scalacheck.cats.ScalaCheckSetup
import org.scalacheck.cats.instances.ArbitraryInstances._

class ArbitraryLaws extends CatsSuite with ScalaCheckSetup {
  // Tests Alternative
  checkAll("Arbitrary", AlternativeTests[Arbitrary].alternative[Int, Int, Int])
  // Tests Monad
  checkAll("Arbitrary", MonadTests[Arbitrary].monad[Int, Int, Int])
  // Tests FunctorFilter
  checkAll("Arbitrary.FunctorFilterLaws", FunctorFilterTests[Arbitrary].functorFilter[Int, Int, Int])

  // Tests Monoid for Inner Given Monoid
  checkAll("Arbitrary[String]", MonoidTests[Arbitrary[String]].monoid)
  // Tests Low Priority Semigroup
  checkAll("Arbitrary[NonEmptyList[Int]]", SemigroupTests[Arbitrary[NonEmptyList[Int]]].semigroup)
}

