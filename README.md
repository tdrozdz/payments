# Bank account managment

Demonstration of bank account management capabilities with simple (mocked) verification steps as
didn't want to focus on that part rather or parrarel executions.

## Introduction

Project expose 3 rest endpoint

- GET accountDetail (localhost:8080/accountDetail?clientId) account information
- POST withdraw (localhost:8080/withdraw) `AccountRequest` as payload
- POST deposit (localhost:8080/accountDetail?clientId) `AccountRequest` as payload

Project has two separate services. The first one is the grpc server which allows to do the remote
client checks:

- Verification that the client has completed registration.
- Verification that the client is not blocked.
- Verification that the client is not sanctioned.

Additionally, there is a local(blocking call) check:

- Ensuring that the client has sufficient funds in their account to perform a withdrawal operation.

Communication with the gRPC server is asynchronous, allowing all calls to execute in parallel.

## How to start the Application

To start the application, import the project into IntelliJ from the Git repository. In the Run
Configuration window, you should find a payments compound configuration. Select the payments
compound configuration and run it.

Two services should be executed `transaction` and `verification`. Transaction app expose a REST
endpoint on port 8080, while Verification app exposes gRPC endpoint on port 9090

Application contains `DataSeeder` which init app with 4 accounts so we can start playing it straight
away.

Availalbe accounts:
"1", "John", "Doe", 1000
"2", "Jane", "Smith", 1500
"3", "Bob", "Johnson", 800
"4", "Micheal", "Bradley", 0

### example REST calls

`curl --location --request POST 'localhost:8080/withdraw' \
--header 'Content-Type: application/json' \
--data-raw '{
"cashAmount": 2,
"clientId": "4"
}'`
Throws exception as client 4 doesn't have enough resources

`curl --location --request POST 'localhost:8080/deposit' \
--header 'Content-Type: application/json' \
--data-raw '{
"cashAmount": 2,
"clientId": "2"
}'`

Works fine

`curl --location --request GET 'localhost:8080/accountDetail?clientId=4' \
--header 'Content-Type: application/json'`

Also works fine