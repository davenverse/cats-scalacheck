# cats-scalacheck [![Build Status](https://travis-ci.org/ChristopherDavenport/cats-scalacheck.svg?branch=master)](https://travis-ci.org/ChristopherDavenport/cats-scalacheck) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12)

## Quick Start

To use cats-scalacheck in an existing SBT project with Scala 2.11 or a later version, add the following dependency to your
`build.sbt`:

```scala
libraryDependencies += "io.chrisdavenport" %% "cats-scalacheck" % "<version>"
```

## Getting Started

```scala
scala> import org.scalacheck.{Gen, Arbitrary}
import org.scalacheck.{Gen, Arbitrary}

scala> import org.scalacheck.cats.implicits._
import org.scalacheck.cats.implicits._

scala> import cats.Applicative
import cats.Applicative

scala> import cats.implicits._
import cats.implicits._

scala> val apComposition: Gen[(Int, String)] = Applicative[Gen].product(
     |   Arbitrary.arbitrary[Int],
     |   Arbitrary.arbitrary[String]
     | )
apComposition: org.scalacheck.Gen[(Int, String)] = org.scalacheck.Gen$$anon$3@70674be4
```

## Instances

### Gen

- `Alternative[Gen]`
- `Monad[Gen]`
- `Monoid[A] => Monoid[Gen[A]]`
- `Semigroup[A] => Semigroup[Gen[A]]`

### Cogen

- `ContravariantSemigroupal[Gen]`
- `MonoidK[Gen]`
