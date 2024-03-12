# Chalender.ch API

## Introduction

This repository contains the API for chalender.ch. 


## Getting Started

To get a copy of this project up and running on your local machine for development and testing purposes, follow these steps:

### Prerequisites

Ensure you have the following installed on your system:

- JDK 17 or later
- Maven
- Docker

### Installation

1. Clone the repository

2. Navigate to the project directory:

3. Build the project with Maven:

    `mvn clean install`

4. Configure the `application.properties` file with your database credentials and other environment-specific settings.

5. Run the application:

mvn spring-boot:run

The API is now up and running and can be accessed at `http://localhost:8082`.

## Usage

To use the chalender.ch API, make HTTP requests to the available endpoints. Below are some examples of commonly used endpoints:

- `POST /api/events` - Create a new calendar event.
- `GET /api/events` - Retrieve all calendar events.
- `GET /api/events/{id}` - Retrieve a specific calendar event by its ID.
- `PUT /api/events/{id}` - Update an existing calendar event.
- `DELETE /api/events/{id}` - Delete a calendar event.

For detailed API documentation, including all endpoints and their descriptions, refer to the API documentation available at `http://localhost:8082/swagger-ui/index.html`.

## Contributing

We welcome contributions to the chalender.ch API! If you have suggestions for improvement or want to contribute code, please feel free to fork the repository and submit a pull request.

## License

This project is licensed under the Mozilla Public License 2.0 (MPL 2.0) - see the [LICENSE](LICENSE.md) file for details.
