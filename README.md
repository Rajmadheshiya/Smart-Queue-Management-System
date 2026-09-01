# Smart Queue Management System

A web-based queue management system built using Java Spring Boot, Thymeleaf, Spring Data JPA, Spring Security, and MySQL.

## Features

### Patient Features

- Register a patient in the queue
- Generate a unique token number
- Join the queue
- Check token status
- View the number of patients ahead
- Prevent duplicate phone numbers from joining the queue

### Admin Features

- Secure admin login
- View all patients
- Search patients
- Edit patient details
- Delete patient records
- Mark patients as completed
- Call the next patient
- View current queue statistics

### Dashboard

The home dashboard displays:

- Total patients
- Waiting patients
- Completed patients
- Current token being served

## Tech Stack

- Java 17
- Spring Boot 3.5.4
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Maven
- HTML
- CSS

## Project Structure

```text
src/main/java/com/raj/smartqueue
│
├── config
│   └── SecurityConfig.java
│
├── controller
│   ├── AdminController.java
│   ├── HomeController.java
│   └── PatientController.java
│
├── entity
│   └── Patient.java
│
├── repository
│   └── PatientRepository.java
│
└── service
    └── PatientService.java
```
