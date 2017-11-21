# Codemotion Workshop

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

`$ open codemotion-library.ipr`

To run tests:

`$ ./gradlew test`

To see the coverage

`$ ./gradlew check`
`$ open build/reports/jacoco/test/html/index.html`

## Exercise 1: add more tests
Look at the jacoco report to understand the parts of the code that need more coverage.

Hint:
Try creating a test for the method _borrow_ in the _CatalogController_
