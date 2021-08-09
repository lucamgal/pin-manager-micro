# pin-manager-micro
PIN Manager Microservice


### Acceptance criteria:

We need to develop a microservice that exposes a restful API.

A first API needs to accept a phone number and return a random, six digits long, pin code.

A second API needs to accept a phone number and a pin code, and verify if the pin code in input equals the one generated in the previous step.
After three failed verification attempts, the second API must keep returning an error (even if eventually the right PIN is sent).
Any attempt to verify a PIN generated more than 20 minutes in the past should fail.
For a given MSISDN we should not have more than 3 open pin verification processes in parallel. The PIN verification process starts in the moment that we generate a PIN and is considered complete either when the PIN is verified successfully, or after 20 minutes.


### Non-functional requirements:

Horizontal scalability (multi-node deployments)


### Technical constrains:

- Implemented in spring boot
- Data persistence in a DB (relational or non-relational)
- Runs as a docker container (optional: prepare a docker-compose file that starts both the service and the database).
