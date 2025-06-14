# 🛒 Product Management REST API

A simple RESTful web service for managing products, built with **Spring Boot**, **Java**, and **PostgreSQL**. The service provides endpoints to create and retrieve products, including automatic currency conversion using the Croatian National Bank (HNB) API.

---

## 📌 Project Description

This application fulfills **Assignment #1: Spring REST API**, demonstrating the use of modern Spring ecosystem tools and design patterns. It includes:
- Product creation with price conversion from EUR to USD (via external HNB API).
- Product listing and lookup.
- Role-based endpoint security using Spring Security.

> ⚠️ There is **no frontend**; this is a pure backend REST API.

---

## 🧰 Tech Stack

| Category        | Technology                            |
|----------------|---------------------------------------|
| Language        | Java 17+                              |
| Build Tool      | Gradle                                |
| Framework       | Spring Boot, Spring MVC               |
| Persistence     | Spring Data JPA                       |
| Database        | PostgreSQL (Dockerized)               |
| Mapping         | MapStruct                             |
| Auth & Security | Spring Security (Basic Auth)          |
| Docs            | Swagger (OpenAPI)                     |
| External API    | HNB Exchange Rate API                 |

---

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Gradle
- Docker + Docker Compose
- Git

---
## 🐙 Clone the repository

```bash


git clone https://github.com/kcu24/product-api-task.git


```

---

### 🐳 Run PostgreSQL via Docker

Navigate to the root of the project and go to the `docker` folder and start the DB container:

```bash
  cd docker
  docker compose up -d

```

---
 
## ⚙️ Application Configuration

Make sure your `application.yaml` contains:

```properties
  datasource:
    url: jdbc:postgresql://localhost:5432/myProductDatabase
    username: myuser
    password: secret


```

---

## 🏗️ Build & Run the App (from root folder)

🔨 Build with Gradle 

```bash
  ./gradlew clean build

```

▶️ Run the Application

```bash
  ./gradlew :productsmgmtapp:bootRun 


```


By default, the app will be available at: http://localhost:8080
---


---

## 🔐 Authentication
This application uses Basic Authentication with in-memory users (just for testing - not a real production case).

### 🔑 Default Users

| Username | Password | Role                                        |
|----------|----------|---------------------------------------------|
| admin    | admin123 | ADMIN (all privileges)                      | 
| user     | user123  | USER (can do all except creating a product) |


---



## 📬 API Endpoints

---
### ➕ Create Product
POST /api/v1/products/

Role required: ADMIN

Request Body
```json

{
"code": "PROD123456",
"name": "Premium Coffee",
"priceInEur": 10.50,
"available": true
}
```

✅ The code must be unique and exactly 10 characters long.

✅ The priceInUsd is automatically fetched from the HNB API upon creation.

✅ The priceInUsd is automatically calculated based on price in EUR and fetched exchange rate from the HNB API upon creation.

✅ id is handled on the DB level




Response Body
```json

{
  "id": 37,
  "code": "PROD123456",
  "name": "Premium Coffee",
  "priceInEur": 10.50,
  "priceInUsd": 12.08,
  "available": true
}
```

---
### 🔍 Get Product by ID
GET /api/v1/products/{id}

Roles allowed: USER, ADMIN

---

### 📋 List All Products (Paginated)
GET /api/v1/products?page=0&size=10&sortBy=name&sortDirection=ASC

Roles allowed: USER, ADMIN

---

---

## 📖 API Documentation
You can access the Swagger UI to explore the API at (the app should be started first):


http://localhost:8080/swagger-ui.html

---

## 🧪 API Testing with Postman

To help you get started with testing the API, a **Postman collection** is provided.

### 📥 Requirements

- Make sure you have [Postman](https://www.postman.com/downloads/) installed on your machine.

### 📁 Import the Collection

1. Open Postman.
2. Click on **"File"** -> **"Import"**.
3. Select the provided `.postman_collection.json` file (located in the postman project directory - postman/product_API.postman_collection.json).
4. The collection will appear in your workspace under **Collections**.

### 🧾 Notes

- This collection includes only **basic test cases** for easier testing (e.g., successful requests, validation errors, duplicate checks).
- Some requests may need to be **adjusted to fit your local environment** (e.g., data persisted in DB, url, etc..).
- For advanced usage of Postman or to learn how to create your own requests, please refer to the [official Postman documentation](https://learning.postman.com/).
---
## 🔧 Development Profile (dev)
This project includes a dedicated dev Spring profile for development and testing purposes.
It uses In-memory H2 database.

▶️ Run the Application in dev profile

```
  ./gradlew :productsmgmtapp:bootRun --args='--spring.profiles.active=dev'

```

