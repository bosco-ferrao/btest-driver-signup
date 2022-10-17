# Spring Boot REST Object Driver CRUD and Patch Example

Driver Class Object has 
 id (auto generated Long),
 name (String),
 address (String),
 readyForRide (Boolean)
 
In memory DB: H2
Preloaded 3 values.

Drawbacks:
I've skipped pagination.

$ curl -v localhost:8080/drivers
```
There are unit tests done:
DriverControllerTest.java
DriverControllerRestTemplateTest.java


Using Postman tested:
1) GET http://localhost:8080/drivers

2) POST http://localhost:8080/drivers
JSON Data:
{
  "name": "Bosco4",
  "address": "Address4",
  "readyForRide": false
}

3) GET http://localhost:8080/drivers/4

4) PUT http://localhost:8080/drivers/3
JSON Data:
{
  "name": "Bosco3.1",
  "address": "Address3.1",
  "readyForRide": false
}

5) PATCH http://localhost:8080/drivers/3
JSON Data:
{
  "address": "Address3.2"
}
Result:
{
    "timestamp": "2022-10-17T08:08:14.582+0000",
    "status": 405,
    "error": "Method Not Allowed",
    "message": "Field [address] update is not allow.",
    "path": "/drivers/3"
}

6) PATCH http://localhost:8080/drivers/3
JSON Data:
{
  "readyForRide": true
}

7) DELETE http://localhost:8080/drivers/4
Note: delete code is kept simple

