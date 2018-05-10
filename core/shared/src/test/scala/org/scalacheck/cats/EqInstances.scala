package org.scalacheck.cats

import cats._
import org.scalacheck._
import rng.Seed
import scala.util._

object EqInstances {
  def sampledCogenEq[A](trials: Int)(implicit ev: Arbitrary[A]): Eq[Cogen[A]] =
    new Eq[Cogen[A]] {
      def eqv(x: Cogen[A], y: Cogen[A]): Boolean = {
        val gen : Gen[A] = ev.arbitrary
        val params : Gen.Parameters = Gen.Parameters.default
        // Loop Function which checks that the seeds from perturbing
        // given cogens create equivalent seeds for x iterations
        // to consider them equal
        def loop(count: Int, retries: Int, seed: Seed): Boolean =
          if (retries <= 0) sys.error("Generator Function Failed")
          else if (count <= 0) true // If make it through count all equal these are equal
          else {
            val rx = gen.doApply(params, seed) // Get Value
            rx.retrieve.fold(
              loop(count, retries - 1, rx.seed) // Loop As Necessary
            ){ a => 
              val seed = Seed.random
              val sx = x.perturb(seed, a)
              val sy = y.perturb(seed, a)
              if (sx != sy) false // If they are not equivalent
              else loop(count - 1, retries, rx.seed) // Another trial
            }
          }
        // Initiate Loop
        loop(trials, trials, Seed.random)
      }
    }
    def sampledGenEq[A: Eq](trials: Int): Eq[Gen[A]] = Eq.instance[Gen[A]]{ case (x, y) =>
      val params = Gen.Parameters.default
      def loop(count: Int, seed: Seed): Boolean =
        if (count <= 0) true 
        else {
          // Leave this so the inequality creates the eq
          val tx = Try(x.doApply(params, seed))  
          val ty = Try(y.doApply(params, seed))
          (tx, ty) match {
            case (Failure(_), Failure(_)) =>
              // They both failed, good, keep going
              loop(count - 1, Seed.random)
            case (Success(rx), Success(ry)) =>
              if (rx.retrieve != ry.retrieve) false
              else loop(count - 1, seed.next)
            case _ =>
              false
          }
        }
    loop(trials, Seed.random)
  }

}