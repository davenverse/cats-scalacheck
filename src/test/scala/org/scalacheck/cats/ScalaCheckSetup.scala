package org.scalacheck.cats

import cats._
import cats.data._
import cats.implicits._
import org.scalacheck._
import org.scalacheck.cats.implicits._
import org.scalacheck.rng.Seed

trait ScalaCheckSetup {
  
  implicit def genEq[A: Eq]: Eq[Gen[A]] =
    EqInstances.sampledGenEq(1000)

  implicit def cogenEq[A: Arbitrary]: Eq[Cogen[A]] =
    EqInstances.sampledCogenEq(1000)

  implicit lazy val arbitrarySeed: Arbitrary[Seed] =
    Arbitrary(Gen.choose(Long.MinValue, Long.MaxValue).map(n => Seed(n)))

  implicit lazy val cogenSeed: Cogen[Seed] =
    Cogen[Long].contramap(_.long._1)

  implicit def arbitraryNonEmptyList[A: Arbitrary]: Arbitrary[NonEmptyList[A]] =
    Arbitrary(
      (Arbitrary.arbitrary[A], Arbitrary.arbitrary[List[A]]).mapN(NonEmptyList(_, _))
    )

  // Better Arbitrary Gen
  implicit def arbitraryGen[A: Arbitrary]: Arbitrary[Gen[A]] = {
    val simple = Gen.const(Arbitrary.arbitrary[A])
    val complex = Arbitrary.arbitrary[Seed => Seed].map { f =>
      Gen.gen((params, seed) => Arbitrary.arbitrary[A].doApply(params, f(seed)))
    }
    Arbitrary(Gen.oneOf(simple, complex))
  }

  implicit def arbitraryCogen[A: Cogen]: Arbitrary[Cogen[A]] =
    Arbitrary(Arbitrary.arbitrary[Seed => Seed].map { f =>
      Cogen((seed, a) => f(Cogen[A].perturb(seed, a)))
    })
}