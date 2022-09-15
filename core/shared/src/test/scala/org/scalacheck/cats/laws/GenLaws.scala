package org.scalacheck.cats.laws


import cats.laws.discipline._
import cats.kernel.laws.discipline._
import cats.data.NonEmptyList
import org.scalacheck.Gen
import org.scalacheck.cats.ScalaCheckSetup
import org.scalacheck.cats.instances.GenInstances._

class GenLaws extends munit.DisciplineSuite with ScalaCheckSetup {

  override def scalaCheckTestParameters =
    super.scalaCheckTestParameters.withMaxSize(20).withMinSuccessfulTests(20)
  
  // Tests Alternative
  checkAll("Gen", AlternativeTests[Gen].alternative[Int, Int, Int])
  // Tests Monad
  checkAll("Gen", MonadTests[Gen].monad[Int, Int, Int])
  // Tests FunctorFilter
  checkAll("Gen.FunctorFilterLaws", FunctorFilterTests[Gen].functorFilter[Int, Int, Int])

  // Tests Monoid for Inner Given Monoid
  checkAll("Gen[String]", MonoidTests[Gen[String]].monoid)
  // Tests Low Priority Semigroup
  checkAll("Gen[NonEmptyList[Int]]", SemigroupTests[Gen[NonEmptyList[Int]]].semigroup)
}


