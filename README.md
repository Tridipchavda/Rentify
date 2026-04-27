# 🏠 Rentify – Property Rental Management System

Rentify is a RESTful backend application built using **Java Spring Boot** and **PostgreSQL** that manages rental properties, tenants, owners, and associated workflows such as contracts, maintenance requests, and rent payments.

---

## 🚀 Tech Stack

* **Backend:** Java, Spring Boot
* **Database:** PostgreSQL
* **Testing:** JUnit, Mockito
* **Build Tool:** Maven
* **Architecture:** RESTful API design

---

## 📦 Features

* CRUD operations for all core entities
* REST-compliant API structure
* Rent payment tracking and reminders
* Maintenance request management
* Unit and integration testing with JUnit & Mockito

---

## 🧩 Core Entities

### 1. Property (property)

Represents rental properties listed by owners.

### 2. Tenant (tenant)

Represents individuals renting properties.

### 3. Owner (owner)

Represents property owners.

### 4. Rental Contract (contract)

Defines rental agreements between tenants and owners.

### 5. Maintenance Request (request)

Tracks issues reported by tenants and their resolution status.

### 6. Payment (payment)

Handles rent payments, due tracking, and payment history.

---

## 🔁 Basic API Endpoints

Each entity follows RESTful conventions with the following endpoints:

| Method | Endpoint             | Description            |
| ------ | -------------------- | ---------------------- |
| GET    | `/api/{entity}/{id}` | Get entity by ID       |
| GET    | `/api/{entity}`      | Get all entities       |
| POST   | `/api/{entity}`      | Create new entity      |
| PUT    | `/api/{entity}/{id}` | Update existing entity |

### Example:

```
GET /api/property/1
GET /api/property
POST /api/property
PUT /api/property/1
```

---

## ⏰ Rent Reminder Service

* Automated system to remind tenants of upcoming or overdue rent payments
* Can be scheduled using Spring Scheduler / Cron Jobs
* Notifications can be extended to email/SMS (future scope)

---

## 🧪 Testing

* **JUnit** used for unit testing
* **Mockito** used for mocking dependencies
* Covers service and controller layers

---

## 🛠️ Setup & Installation

1. Clone the repository:

```
git clone https://github.com/your-username/rentify.git
cd rentify
```

2. Configure PostgreSQL in `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/rentify
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build and run the application:

```
mvn clean install
mvn spring-boot:run
```

---

## 📂 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── models/
     ├── dto/
     ├── enums/   
 ├── computations/
 └── utils/
```

---

## 🔮 Future Enhancements

* Authentication & Authorization (JWT / OAuth2)
* Email/SMS notification integration
* Frontend UI (React / Angular)
* Role-based dashboards
* Payment gateway integration

---

## 📄 License

This project is licensed under the MIT License.

---

## 🤝 Contribution

Contributions are welcome! Feel free to fork the repo and submit pull requests.

---

## 📬 Contact

For any queries or suggestions, reach out via GitHub Issues.

---
