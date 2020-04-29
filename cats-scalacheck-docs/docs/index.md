---
layout: home

---
# cats-scalacheck [![Build Status](https://travis-ci.org/ChristopherDavenport/cats-scalacheck.svg?branch=master)](https://travis-ci.org/ChristopherDavenport/cats-scalacheck) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12)

## Quick Start

To use cats-scalacheck in an existing SBT project with Scala 2.11 or a later version, add the following dependency to your
`build.sbt`:

```scala
libraryDependencies += "io.chrisdavenport" %% "cats-scalacheck" % "<version>"
```

## Getting Started

```scala mdoc
import org.scalacheck.{Gen, Arbitrary}
import org.scalacheck.cats.implicits._
import cats.Applicative

val apComposition: Gen[(Int, String)] = Applicative[Gen].product(
  Arbitrary.arbitrary[Int],
  Arbitrary.arbitrary[String]
)
```

## Instances

### Gen

- `Alternative[Gen]`
- `Monad[Gen]`
- `FunctorFilter[Gen]`
- `Monoid[A] => Monoid[Gen[A]]`
- `Semigroup[A] => Semigroup[Gen[A]]`

### Cogen

- `ContravariantSemigroupal[Gen]`
- `MonoidK[Gen]`