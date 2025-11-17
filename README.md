Intelligent Personal Finance & Subscription Tracker

A clean, modular, microservices-based platform to track personal expenses, manage subscriptions, analyze spending behavior, and provide AI-driven financial insights. The system integrates OCR for receipt extraction, Redis caching for performance, and can scale through containerized deployment.

1. Overview

This project provides users with a comprehensive personal finance management system powered by modern backend and frontend technologies. It supports multiple microservices, predictive analytics, secure authentication, and a responsive Angular-based dashboard.

2. Features
Expense Management

Add, update, and delete expenses

Automatic categorization using ML models

OCR-based receipt extraction

Support for bulk uploads (future enhancement)

Subscription Tracking

Track recurring subscriptions

Predict renewal cycles

Automated reminders and alerts

Analytics & Insights

Monthly and weekly insights

Category-level spending breakdown

Spending trends and financial health scoring

Historical analysis and anomaly detection

Security

JWT authentication

Role-based access control

Secure password encryption

Performance & Scalability

Redis caching for fast analytics

Asynchronous processing for ML/OCR

Fully containerized microservices

3. System Architecture

Frontend:
Angular (finance-app-angular)

Backend:
API Gateway + multiple Spring Boot microservices:

User Service

Expense Service

Subscription Service

Analytics Service

Notification Service

OCR Service

Storage & Infrastructure:

PostgreSQL

Redis

Object Storage (S3/MinIO)

Docker & Kubernetes-ready

A detailed architecture diagram will be added under:
/docs/architecture-diagram.png

4. Repository Structure
intelligent-finance-tracker/
│
├── backend/
│   ├── api-gateway/
│   ├── user-service/
│   ├── expense-service/
│   ├── subscription-service/
│   ├── analytics-service/
│   ├── notification-service/
│   └── ocr-service/
│
├── frontend/
│   └── finance-app-angular/
│
├── devops/
│   ├── docker-compose.yml
│   └── k8s/
│
├── docs/
│   ├── architecture-diagram.png
│   ├── api-spec.md
│   └── erd.png
│
└── README.md

5. Tech Stack
Backend

Java 17+

Spring Boot 3.x

Spring Security, JWT

Spring Data JPA / Hibernate

PostgreSQL

Redis

Docker & Docker Compose

Frontend

Angular 15+

TypeScript

Angular Material

ML & OCR

Tesseract OCR or Google Vision

OpenAI API or custom ML models

6. Running the Project Locally
Start Backend (Docker)
cd devops
docker-compose up --build

Start Frontend
cd frontend/finance-app-angular
npm install
ng serve

7. Development Roadmap
Phase 1 – Core Microservices

User Service (Auth + JWT)

Expense Service

Subscription Service

API Gateway

Phase 2 – Advanced Features

OCR Integration

ML-based categorization

Analytics Service

Notification Service

Phase 3 – Deployment & CI/CD

GitHub Actions

Docker optimizations

Kubernetes deployment

Monitoring & logging

8. Author

Saumya Sakshi
Java Full Stack Engineer
