package org.scalacheck.cats.instances

import cats._
import cats.implicits._
import org.scalacheck._

object ArbitraryInstances extends ArbitraryInstances

trait ArbitraryInstances extends ArbitraryInstances1

sealed private[instances] trait ArbitraryInstances1 extends ArbitraryInstances0 {
  import GenInstances._

  implicit val arbitraryInstances : StackSafeMonad[Arbitrary] with Alternative[Arbitrary] with FunctorFilter[Arbitrary] = 
    new StackSafeMonad[Arbitrary] with Alternative[Arbitrary] with FunctorFilter[Arbitrary] {
    // Members declared in cats.Applicative
    override def pure[A](x: A): Arbitrary[A] =
      Arbitrary(Gen.const(x))

    // Members declared in cats.FlatMap
    override def flatMap[A, B](fa: Arbitrary[A])(f: A => Arbitrary[B]): Arbitrary[B] = 
      fa.flatMap(f)

    override def combineK[A](x: Arbitrary[A], y: Arbitrary[A]): Arbitrary[A] = Arbitrary(
      MonoidK[Gen].combineK(x.arbitrary, y.arbitrary)
    )

    override def empty[A]: Arbitrary[A] = Arbitrary(MonoidK[Gen].empty[A])

    override def map2Eval[A, B, Z](fa: Arbitrary[A], fb: Eval[Arbitrary[B]])(f: (A, B) => Z): Eval[Arbitrary[Z]] =
      fb.flatMap(arb => Eval.later(Applicative[Gen].map2(fa.arbitrary, Gen.lzy(arb.arbitrary))(f))).map(Arbitrary(_))
    
    override def product[A, B](fa: Arbitrary[A], fb: Arbitrary[B]): Arbitrary[(A, B)] =
      Arbitrary(Applicative[Gen].product(fa.arbitrary, fb.arbitrary))

    override def functor: Functor[Arbitrary] = this

    override def mapFilter[A, B](fa: Arbitrary[A])(f: A => Option[B]): Arbitrary[B] =
      fa.flatMap { a =>
        f(a) match {
          case Some(b) => pure(b)
          case _       => Arbitrary(Gen.fail)
        }
      }
  }

  implicit def genMonoid[A: Monoid]: Monoid[Arbitrary[A]] = new Monoid[Arbitrary[A]]{
    override def empty: Arbitrary[A] = Arbitrary(Gen.const(Monoid[A].empty))
    override def combine(x: Arbitrary[A], y: Arbitrary[A]) = Arbitrary(
      for {
        xa <- x.arbitrary
        ya <- y.arbitrary
      } yield xa |+| ya
    )
  }

}

sealed private[instances] trait ArbitraryInstances0 {
  implicit def genSemigroup[A: Semigroup]: Semigroup[Arbitrary[A]] = new Semigroup[Arbitrary[A]]{
    override def combine(x: Arbitrary[A], y: Arbitrary[A]) = Arbitrary(
      for {
        xa <- x.arbitrary
        ya <- y.arbitrary
      } yield xa |+| ya
    )
  }
}
