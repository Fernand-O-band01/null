<div align="center">
  <img src="./assets/null_logo.png" alt="Null Logo" width="200">
  
  <h1>Null</h1>
  
  <p>
    <strong>Un clon de Discord desarrollado desde cero con Arquitectura Moderna.</strong>
  </p>

  <p>
    <img src="https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular" />
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
    <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white" alt="Kafka" />
    <img src="https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB" />
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  </p>
</div>

---

## 📋 Descripción

**Null** es una aplicación de comunicación inspirada en Discord. Este proyecto representa un hito fundamental en mi carrera como desarrollador, marcando la transición hacia arquitecturas escalables. 

El proyecto está estructurado como un **Monorepo**, separando claramente las responsabilidades del cliente (Frontend SPA) y el servidor (Backend), e integrando infraestructura contenerizada para bases de datos, mensajería asíncrona y pruebas de correo electrónico.

## 🚀 Características Principales

* **Autenticación y Seguridad:** Registro seguro de usuarios y validación de cuentas mediante códigos de activación (OTP) enviados por correo electrónico.
* **Arquitectura Orientada a Eventos:** Uso de Apache Kafka para procesar de forma asíncrona notificaciones y correos, desacoplando el servicio principal.
* **Plantillas de Correo Dinámicas:** Correos HTML responsivos y personalizados generados con Thymeleaf (con imágenes incrustadas via CID).
* **Comunicación en Tiempo Real (En desarrollo):** Preparando el terreno para el uso de WebSockets (STOMP & SockJS) para mensajería instantánea.
* **Infraestructura Contenerizada:** Entorno de desarrollo unificado con Docker Compose.

## 🛠️ Tecnologías Utilizadas

### Backend (`/null_services_backend`)
* **Java 17+ & Spring Boot 3** (Web, Security, Data MongoDB, Mail)
* **Apache Kafka** (Broker de mensajería para microservicios)
* **Thymeleaf** (Motor de plantillas para correos)
* **Lombok** (Reducción de código repetitivo)

### Frontend (`/null_services_frontend`)
* **Angular** (Single Page Application - CSR)
* **HTML5 & CSS/SCSS** (Diseño moderno e interfaces dinámicas)

### Infraestructura & Bases de Datos
* **Docker & Docker Compose**
* **MongoDB** (Persistencia NoSQL)
* **Maildev / Mailhog** (Servidor SMTP local para pruebas de correo)

## 📁 Estructura del Monorepo

```text
null/
├── docker-compose.yml         # Orquestación de infraestructura (Mongo, Kafka, Maildev)
├── null_services_backend/     # API REST y lógica de negocio en Spring Boot
└── null_services_frontend/    # Cliente web en Angular
