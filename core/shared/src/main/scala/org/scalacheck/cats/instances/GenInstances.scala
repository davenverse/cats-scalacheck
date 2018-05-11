package org.scalacheck.cats.instances

import cats._
import cats.implicits._
import org.scalacheck.Gen

object GenInstances extends GenInstances

trait GenInstances extends GenInstances1

sealed private[instances] trait GenInstances1 extends GenInstances0 {
  implicit val genInstances : Monad[Gen] with Alternative[Gen] = new Monad[Gen] with Alternative[Gen] {
    // Members declared in cats.Applicative
    override def pure[A](x: A): Gen[A] =
      Gen.const(x)

    // Members declared in cats.FlatMap
    override def flatMap[A, B](fa: Gen[A])(f: A => Gen[B]): Gen[B] = 
      fa.flatMap(f)
    override def tailRecM[A, B](a: A)(f: A => Gen[Either[A,B]]): Gen[B] =
      Gen.tailRecM(a)(f)

    override def combineK[A](x: Gen[A], y: Gen[A]): Gen[A] = Gen.gen{ (params, seed) => 
      val xGen = x.doApply(params, seed)
      if (xGen.retrieve.isDefined) xGen
      else y.doApply(params, seed)
    }

    override def empty[A]: Gen[A] = Gen.fail

    override def product[A, B](fa: Gen[A], fb: Gen[B]): Gen[(A, B)] = Gen.zip(fa, fb)
  }

  implicit def genMonoid[A: Monoid]: Monoid[Gen[A]] = new Monoid[Gen[A]]{
    override def empty: Gen[A] = Gen.const(Monoid[A].empty)
    override def combine(x: Gen[A], y: Gen[A]) = for {
      xa <- x
      ya <- y
    } yield xa |+| ya
  }

}

sealed private[instances] trait GenInstances0 {
  implicit def genSemigroup[A: Semigroup]: Semigroup[Gen[A]] = new Semigroup[Gen[A]]{
    override def combine(x: Gen[A], y: Gen[A]) = for {
      xa <- x
      ya <- y
    } yield xa |+| ya
  }
}
