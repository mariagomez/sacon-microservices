# SACON NY 2019 Workshop

## Requirements
* Github account
* Java 8
* IntelliJ IDEA (enterprise edition recommended but community would also work)
* [For Windows only] git bash
* [Optional] TravisCI linked to your Github account
* [Optional] Codecov linked to your Github account

## Setup local environment

Go to the library-monolith directory:

`cd library-monolith`

To run:

`$ ./gradlew bootRun`

To edit with IntelliJ:

`$ ./gradlew idea`

`$ open library-monolith.ipr`

To run tests:

`$ ./gradlew test`

To see the coverage

`$ ./gradlew check`
`$ open build/reports/jacoco/test/html/index.html`

## Exercise 1: add more tests
Look at the jacoco report to understand the parts of the code that need more coverage.

Hint:
Try creating a test for the method _borrow_ in the _CatalogController_

You can follow a possible solution in [Exercise1](Exercise1.md)

or you could checkout the branch:
````
$ git fetch
$ git checkout 1_monolith_with_tests
````

## Exercise 2: re-organize monolith and create clear separation of packages
The goal of these exercises is to end up with three distinct packages that express the different bounded contexts:

```
com.oreilly.sacon.library.availability
com.oreilly.sacon.library.catalog
com.oreilly.sacon.library.rating
```

*Hint:*
Use refactoring techniques like _composing methods_ and _moving features between objects_. Don't be afraid of _organizing data_.

You can follow a possible solution in [Exercise2-part1](Exercise2-part1.md) and [Exercise2-part2](Exercise2-part2.md)

or you could checkout the branch:
`$ git checkout 2_monolith_with_packages`
