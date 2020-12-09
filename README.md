mtd-tax-calculation
========================

[![Apache-2.0 license](http://img.shields.io/badge/license-Apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

The MTD Tax Calculation microservice allows a developer to retrieve the result of a previously triggered tax calculation.

## Requirements
- Scala 2.12.x
- Java 8
- sbt 1.3.13
- [Service Manager](https://github.com/hmrc/service-manager)

## Development Setup
Run the microservice from the console using: `sbt run` (starts on port 9770 by default)

Start the service manager profile: `sm --start MTDFB_SA_TAXC`
 
## Run Tests
Run unit tests: `sbt test`

Run integration tests: `sbt it:test`

## Reporting Issues
You can create a GitHub issue [here](https://github.com/hmrc/mtd-tax-calculation/issues)

## License
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")