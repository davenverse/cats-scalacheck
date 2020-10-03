package org.scalacheck.cats

import cats.data.Ior
import org.scalacheck.Arbitrary

package object arbitrary {
  implicit def arbIor[A, B](implicit aa: Arbitrary[A], ab: Arbitrary[B]): Arbitrary[Ior[A, B]] = Arbitrary(generators.ior(aa.arbitrary, ab.arbitrary))
}
