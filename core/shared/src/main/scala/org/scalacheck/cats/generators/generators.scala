package org.scalacheck.cats

import cats.data.Ior
import org.scalacheck.Gen

package object generators {
  def ior[A, B](ga: Gen[A], gb: Gen[B]): Gen[Ior[A, B]] = Gen.oneOf(ga.map(Ior.left), gb.map(Ior.right), ga.flatMap(genA => gb.map(Ior.Both(genA, _))))
}
