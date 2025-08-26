# AI Fitness App

A microservices-based fitness activity tracker with AI-powered recommendations.

## Architecture

- **User Service:** Java Spring Boot, PostgreSQL
- **Activity Service:** Java Spring Boot, MongoDB, RabbitMQ
- **AI Service:** Java Spring Boot, MongoDB, Gemini API
- **Gateway:** Spring Cloud Gateway, Keycloak JWT authentication
- **Service Discovery:** Eureka Server
- **Centralized Config:** Spring Cloud Config Server
- **Frontend:** React, Material UI, OAuth2 PKCE

## Tech Stack

- Java 17, Spring Boot, Maven
- PostgreSQL (User Service)
- MongoDB (Activity & AI Service)
- RabbitMQ (Activity & AI Service)
- Eureka Server
- Spring Cloud Config Server
- Keycloak (OAuth2/JWT)
- React, JavaScript, npm, Material UI
- Redux Toolkit

## Prerequisites

- JDK 17+
- Maven
- Node.js & npm
- PostgreSQL
- MongoDB
- RabbitMQ
- Keycloak
- Docker (optional for dependencies)
RabbitMQ : docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:4-management
Keycloak : docker run -p 127.0.0.1:8181:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:26.3.3 start-dev


## Setup Instructions

### 1. Clone the repository

```sh
git clone https://github.com/sneha-16hub/AI-Fitness-App.git
cd AI-Fitness-App
