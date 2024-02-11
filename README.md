# Endpoints Help

This document provides an overview and testing guidance for a Spring Boot project integrating with AWS Cognito for authentication and authorization.

## Overview

The project includes four REST API endpoints, each demonstrating different aspects of security integration with AWS Cognito IDP. The endpoints are designed to handle authentication and authorization, parsing Cognito groups and scopes into granted authorities.

## Requirements

- Java 21
- Spring Boot 3
- Maven
- AWS Cognito Instance

### Clean build whole project from command line
```
mvn clean install
```

## Endpoints

### 1. `/unauthenticated`

- **Description**: An open endpoint that requires no authentication.
- **Attributes Returned**:
    - `message`: "Free for all to see"
    - `name`: "Anonymous"

### 2. `/authenticated`

- **Description**: Secured endpoint requiring authentication.
- **Attributes Returned**:
    - `message`: "Looks like you have been authenticated"
    - `name`: Concatenation of `<given_name>` `<family_name>`
    - `grantedAuthorities`: List of Granted Authorities

### 3. `/vendors`

- **Description**: Secured endpoint requiring the user to be a member of the "VENDOR" group.
- **Attributes Returned**:
    - `message`: "Welcome to the vendor group!"
    - `name`: Concatenation of `<given_name>` `<family_name>`
    - `grantedAuthorities`: List of Granted Authorities

### 4. `/vendors/vid`

- **Description**: Secured endpoint requiring membership in the "VENDOR" group and a specific VID.
- **Attributes Returned**:
    - `message`: "Looks like you’re a specific vendor!"
    - `vid`: Specified VID
    - `name`: Concatenation of `<given_name>` `<family_name>`
    - `grantedAuthorities`: List of Granted Authorities

## Suggested Test Cases

### `/unauthenticated`

- **Test Case**: Access without authentication.
    - **Expected Outcome**: Success response with the message "Free for all to see" and name "Anonymous".

### `/authenticated`

- **Test Case 1**: Access without authentication.
    - **Expected Outcome**: Authentication error or redirect to login.
- **Test Case 2**: Access with valid authentication.
    - **Expected Outcome**: Success response with user's name and a list of granted authorities.

### `/vendors`

- **Test Case 1**: Access without authentication.
    - **Expected Outcome**: Authentication error or redirect to login.
- **Test Case 2**: Access with authentication but not part of the "VENDOR" group.
    - **Expected Outcome**: Authorization error.
- **Test Case 3**: Access with authentication and part of the "VENDOR" group.
    - **Expected Outcome**: Success response welcoming to the vendor group with user's name and authorities.

### `/vendors/2cc40d4d-36c7-4c60-b15c-761dded8abb5`

- **Test Case 1**: Access without authentication.
    - **Expected Outcome**: Authentication error or redirect to login.
- **Test Case 2**: Access with authentication but not part of the "VENDOR" group or incorrect VID.
    - **Expected Outcome**: Authorization error.
- **Test Case 3**: Access with authentication, part of the "VENDOR" group, and correct VID.
    - **Expected Outcome**: Success response indicating specific vendor status with VID, user's name, and authorities.

## Conclusion

This guide outlines the setup and suggested test cases for a Spring Boot application with AWS Cognito integration. Ensure thorough testing to validate both the security aspects and the functional requirements of each endpoint.
