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

The most detailed and actual REST API documentation is generated at build time in the `openapi.json` file.
Also, available after running locally at http://localhost:8080/v3/api-docs and http://localhost:8080/swagger-ui/index.html.

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "Users Aggregation Service",
    "description": "Service for aggregating users data from multiple databases",
    "version": "0.0.1"
  },
  "servers": [
    {
      "url": "http://localhost:8080",
      "description": "Generated server url"
    }
  ],
  "paths": {
    "/users": {
      "get": {
        "tags": [
          "user-controller"
        ],
        "summary": "Get aggregated users from all databases",
        "operationId": "getUsers",
        "parameters": [
          {
            "name": "ids",
            "in": "query",
            "description": "IDs of users to be searched",
            "required": false,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "usernames",
            "in": "query",
            "description": "Usernames of users to be searched",
            "required": false,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "names",
            "in": "query",
            "description": "Names of users to be searched",
            "required": false,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "surnames",
            "in": "query",
            "description": "Surnames of users to be searched",
            "required": false,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          }
        ],
        "responses": {
          "400": {
            "description": "Request parameters are empty",
            "content": {
              "application/problem+json": {}
            }
          },
          "200": {
            "description": "Found users",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/User"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "User": {
        "type": "object",
        "properties": {
          "id": {
            "type": "string"
          },
          "username": {
            "type": "string"
          },
          "name": {
            "type": "string"
          },
          "surname": {
            "type": "string"
          }
        }
      }
    }
  }
}
```
