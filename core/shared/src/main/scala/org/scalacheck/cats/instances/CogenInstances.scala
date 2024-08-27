/*
 * Copyright 2018 Davenverse
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scalacheck.cats.instances

import cats._
import org.scalacheck._

object CogenInstances extends CogenInstances

trait CogenInstances {

  implicit val cogenContravariantSemigroupal: ContravariantSemigroupal[Cogen] =
    new ContravariantSemigroupal[Cogen] {
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
        Cogen((seed, _) => seed)
      def combineK[A](x: Cogen[A], y: Cogen[A]): Cogen[A] =
        Cogen((seed, a) => y.perturb(x.perturb(seed, a), a))
    }
}
