# cats-scalacheck [![Build Status](https://travis-ci.org/ChristopherDavenport/cats-scalacheck.svg?branch=master)](https://travis-ci.org/ChristopherDavenport/cats-scalacheck) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.chrisdavenport/cats-scalacheck_2.12)

Inspiration Was Taken From the never published cats-check. Instances for Cats for scalacheck types. So all credit to [erik-stripe](https://github.com/erik-stripe) and the last maintainer [mdedetrich](https://github.com/mdedetrich) for their original work on this that helped me build this.

## Quick Start

To use cats-scalacheck in an existing SBT project with Scala 2.11 or a later version, add the following dependency to your
`build.sbt`:

```scala
libraryDependencies += "io.chrisdavenport" %% "cats-scalacheck" % "<version>"
```

## Getting Started

```scala
import org.scalacheck._
import org.scalacheck.cats.implicits._
import cats.implicits._

val apComposition: Gen[(Int, String)] = Arbitrary.arbitrary[Int] <*> Arbitrary.arbitrary[String]
```

## Instances

### Gen

- `Alternative[Gen]`
- `Monad[Gen]`
- `FunctorFilter[Gen]`
- `Monoid[A] => Monoid[Gen[A]]`
- `Semigroup[A] => Semigroup[Gen[A]]`

### Cogen

- `ContravariantSemigroupal[Cogen]`
- `MonoidK[Cogen]`

## Why in org.scalacheck

This was necessary because scalacheck makes some of their instances package private that
are required to roll these meaningfully.
