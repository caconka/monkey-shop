# Monkey-shop api

The objective is to create a REST API to manage customer data for a small shop. It
will work as the backend side for a CRM interface that is being developed by a
different team.

- [Dependencies](#dependencies)
- [Building](#building)
- [Swagger](src/test/swagger/swagger.yaml)
- [Architecture](#architecture)
- [Authorization](#authorization)
- [Notes](#notes)

## Dependencies

- Gradle
- Docker
- Docker-compose
- Postman for manual testing ([collection](src/test/postman/monkey-shop.postman_collection.json))

## Building

To run your application:
```
./run.sh
```

To launch your tests:
```
./gradlew clean test
```

To package your application:
```
./gradlew clean assemble
```

## Architecture

The project has a hexagonal architecture with the following directories:
- config: Classes that abstract the configuration, so that the same docker image can be deployed on different environment.
- error: Classes representing the project error model.
- logger: Classes to use the same way of printing logs.
- verticle: Classes that start the running workers.
- domain: Classes that define the business logic, regardless of any other component that may interact with this one. Distributed in the following packages:
	- core: Classes representing the context used by all layers.
	- model: Classes representing the business logic model.
	- logic. Classes implementing the business logic operations.
	- port: Interfaces of the external components needed to perform the business logic operations.
- primary: Classes that receive events (e.e. REST requests). Distributed in the following packages:
	- model: Classes representing the received event data model.
	- adapter: Classes that translate between the received event data model and the business logic model.
	- handler: Classes that receive an event and may send a response (i.e. HTTP response).
- secondary: Classes that integrate third party components needed to perform the business logic operations. Distributed in the following packages:
	- model: Classes representing the data model of the third party component.
	- adapter: Classes that translate between the business logic model and the data model of the third party component.
	- dao: Classes that connect and use the third party components. It is advisable to have two types of classes:
		- Clients, that only connect to the third party components.
		- DAOs, that use clients and adapters to implement the interfaces defined in domain.port.
		- Client interfaces. Abstraction layer between DAOs and clients, which will be used as mocking point in unit tests.

This is a sample directory structure.
```
src
├── main
│   ├── java
│   │   └── com
│   │       └── monkey
│   │           └── monkeyshop
│   │               ├── config
│   │               ├── di
│   │               ├── domain
│   │               │   ├── core
│   │               │   ├── logic
│   │               │   ├── model
│   │               │   │   └── command
│   │               │   └── port
│   │               ├── error
│   │               │   ├── domain
│   │               │   ├── exceptions
│   │               │   └── handler
│   │               ├── logger
│   │               ├── primary
│   │               │   ├── adapter
│   │               │   ├── handler
│   │               │   └── model
│   │               ├── secondary
│   │               │   ├── crm
│   │               │   │   ├── dao
│   │               │   │   └── model
│   │               │   └── postgres
│   │               │       ├── adapter
│   │               │       ├── dao
│   │               │       └── model
│   │               └── verticles
│   └── resources
│       └── db
│           └── migration
└── test
    ├── java
    │   └── com
    │       └── monkey
    │           └── monkeyshop
    ├── postman
    ├── resources
    └── swagger
```

## Authorization

The project has a postgreSQL table to manage the authentication and authorization of users. This table is created by flyway with this [script](src/main/resources/db/migration/V1.0.0__init.sql).

A JWT authentication solution has been chosen.

- Resources under /users will only be allowed by the admin role.
- Resources under /customers will only be allowed by logged-in users.

A user with admin role is created at the start of the project. This user can start creating users with a password. The user created and logged-in with these credentials can then update this password.

## Notes

- The secondary.crm package is not implemented. It would be the connector to the CRM interface to call the resources. Once the CRM interface is defined, it would be the only thing to modify.
