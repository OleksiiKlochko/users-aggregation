# User Aggregation Service

## Overview

A service for aggregating users data from multiple databases. The application
provides a single rest endpoint to get users from all databases. Also, it provides selecting filters
through request parameters.
Currently,
supported [PostgreSQL](https://www.postgresql.org/), [Oracle](https://www.oracle.com/), [MySql](https://www.mysql.com/)
databases. Others are under development.

## Requirements

To build and run the application you need:

- [JDK 21+](https://www.java.com/)
- [Maven 3.8.x+](https://maven.apache.org/)

## Configuration

To add databases just put the properties into the `application.yml` file in the following format:

```yml
data-sources:
  - name: data-base-1
    strategy: postgres
    url: jdbc://.....
    table: users
    user: testuser
    password: testpass
    mapping:
      id: user_id
      username: login
      name: first_name
      surname: last_name
  - name: data-base-2
    strategy: oracle
    url: jdbc://.....
    table: user_table
    user: testuser
    password: testpass
    mapping:
      id: ldap_login
      username: ldap_login
      name: name
      surname: surname
```

## Build

`mvn clean install`

## Run

`mvn spring-boot:run`

## REST API example

Request

`GET /users` or with optional filters `/users?ids=id1,id2&usernames=username1,username2,names=name1,name2&surnames=surname1,surname2`

Response

```json
[
  {
    "id": "example-user-id-1",
    "username": "user-1",
    "name": "User",
    "surname": "Userenko"
  },
  {
    "id": "example-user-id-2",
    "username": "user-2",
    "name": "Testuser",
    "surname": "Testov"
  }
]
```

## Documentation

The actual Open API documentation is located in the 
[`users-aggregation.yml`](https://github.com/OleksiiKlochko/users-aggregation/tree/master/src/main/resources/users-aggregation.yml) file.
