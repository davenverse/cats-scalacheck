---
layout: home

---
# cats-scalacheck [![Build Status](https://github.com/Tomahna/cats-scalacheck/workflows/CI/badge.svg)](https://github.com/Tomahna/cats-scalacheck/workflows/CI/badge.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12)

## Quick Start

`cats-scalacheck` is published for scala 2.12 and 2.13, and scalajs 1.0.0. If you require scalajs 0.6 and/or scala 2.11, you may use the last version of this project: `0.2.0`

To use cats-scalacheck in an existing SBT project, add the following dependency to your `build.sbt`:

```scala
libraryDependencies += "io.chrisdavenport" %% "cats-scalacheck" % "<version>"
```

For use with scalajs 1.0.x:

```scala
libraryDependencies += "io.chrisdavenport" %%% "cats-scalacheck" % "<version>"
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
