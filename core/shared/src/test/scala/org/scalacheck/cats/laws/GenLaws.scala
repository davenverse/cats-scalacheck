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
