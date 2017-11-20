# testonia
Functional-Test framework

# testonia modules
Testonia is divided into 6 modules for better code management:
* testonia-model
* testonia-core
* testonia-bridge
* testonia-dal
* testonia-business
* testonia-example

## testonia-model
* It represents "domain model" of testonia. Includes various POJOs used by testonia framework

## testonia-core
* It represents "core" framework of testonia including ExecutionUnit, Component, Task, Asserter, TestExecutor, ComponentExecutor. Also includes basic guice and reporting related functionality.

## testonia-bridge
* includes classes that serve to call external REST, ASF, Protobuf endpoints
* A single bridge class represents unique endpoint with its own port. If you are adding 2 APIs exposed on same port, add them to the same class.  

## testonia-dal
* It represents DAL layer of testonia. Includes various DAO classes that makes DB calls
* A package in this module represents database
* A class inside the package represents table inside the database

## testonia-business
* It represents business (service) layer of testonia. It has common components such as tasks & asserters, which are used by different fulfillment FT(functional test) projects

## testonia-example
* It includes sample usecase of how to use testonia. It serves as a starting point for new FT project.
* Please don't add actual tests here since this module is meant for demonstration purposes only

