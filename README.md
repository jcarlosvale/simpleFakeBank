# Financial FAKE API
by João Carlos (https://www.linkedin.com/in/joaocarlosvale/)

This project consists of a sandbox Java banking application that can be used for:
* Create a new bank account for a customer, with an initial deposit amount. A
  single customer may have multiple bank accounts.
* Transfer amounts between any two accounts, including those owned by
  different customers.
* Retrieve balances for a given account.
* Retrieve transfer history for a given account.

## Technologies used:
* Java 11
* Spring
* Spring Boot
* H2 database engine (https://www.h2database.com/html/main.html): embedded relational database
* Maven
* SWAGGER

## Available endpoints
1. POST http://localhost:8080/v1/accounts <br>
   Creates a new account. <br>
   Input:
````json
{
"initial_amount": 0,
"user_id": 0
}
````
Output:
````json
{
  "body": {},
  "statusCode": "ACCEPTED",
  "statusCodeValue": 0
}
````
   
2. GET http://localhost:8080/v1/accounts/{id}<br>
Retrieves the account balance<br>
Output
````json
{
  "account_id": 0,
  "balance": 0,
  "created_at": "2021-04-01T17:26:40.264Z"
}
````
   
3. POST http://localhost:8080/v1/operations<br>
Creates a transfer between accounts<br>
Input
````json
{
  "receiver_account_id": 0,
  "sender_account_id": 0,
  "value": 0
}
````
Output
````json
{
  "body": {},
  "statusCode": "ACCEPTED",
  "statusCodeValue": 0
}
````
4. GET http://localhost:8080/v1/operations/fromAccount/{accountId}<br>
Get historic of operations, given an account id.<br>
Output
````json
{
  "account_id": 0,
  "created_at": "2021-04-01T17:29:13.493Z",
  "operations": [
    {
      "created_at": "2021-04-01T17:29:13.493Z",
      "operation_id": 0,
      "receiver_account_id": 0,
      "sender_account_id": 0,
      "value": 0
    }
  ]
}
````   
   
## Commands:

To run:

    mvn spring-boot:run

To compile, test:

    mvn clean install

## API Documentation:
First start the application and visit:
    http://localhost:8080/swagger-ui/
