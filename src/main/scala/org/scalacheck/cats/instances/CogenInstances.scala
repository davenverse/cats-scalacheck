package org.scalacheck.cats.instances

import cats._
import org.scalacheck._

object CogenInstances extends CogenInstances

trait CogenInstances{

  implicit val cogenContravariantSemigroupal: ContravariantSemigroupal[Cogen] = 
    new ContravariantSemigroupal[Cogen]{
      // Members declared in cats.Contravariant
      def contramap[A, B](fa: Cogen[A])(f: B => A): Cogen[B] = 
        fa.contramap(f)
  
      // Members declared in cats.Semigroupal
      def product[A, B](fa: Cogen[A], fb: Cogen[B]): Cogen[(A, B)] = 
        Cogen.tuple2(fa, fb)
    }


  implicit val cogenMonoidK: MonoidK[Cogen] =
    new MonoidK[Cogen] {
      def empty[A]: Cogen[A] =
        Cogen { (seed, _) => seed }
      def combineK[A](x: Cogen[A], y: Cogen[A]): Cogen[A] =
        Cogen { (seed, a) => y.perturb(x.perturb(seed, a), a) }
  }
}