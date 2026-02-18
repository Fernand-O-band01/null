# 🌑 Null - Discord Clone

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![WebSocket](https://img.shields.io/badge/WebSocket-010101?style=for-the-badge&logo=socket.io&logoColor=white)

## 📋 Descripción

**Null** es una aplicación de chat en tiempo real inspirada en Discord. Este proyecto representa un hito importante en mi carrera como desarrollador, ya que es **mi primer proyecto Full Stack desarrollado completamente por mi cuenta**.

El objetivo principal fue entender y aplicar la arquitectura de WebSockets para la comunicación bidireccional, la persistencia de datos NoSQL y la gestión de estados de usuario en tiempo real.

## 🚀 Características Principales

* **Comunicación en Tiempo Real:** Chat fluido utilizando WebSockets (STOMP & SockJS).
* **Usuarios Conectados:** Lista dinámica de usuarios que muestra quién está *Online* y *Offline* al instante.
* **Mensajería Privada:** Envio de mensajes 1 a 1 dirigidos a usuarios específicos.
* **Persistencia de Datos:** Historial de chat guardado en MongoDB (los mensajes no se pierden al recargar).
* **Notificaciones Visuales:** Indicadores de mensajes nuevos cuando el chat no está activo.

## 🛠️ Tecnologías Utilizadas

### Backend
* **Java 17**
* **Spring Boot 3** (Web, WebSocket, Data MongoDB)
* **Lombok** (Para reducir el código repetitivo)

### Frontend
* **HTML5 & CSS3** (Diseño responsivo y moderno)
* **Vanilla JavaScript** (Lógica del cliente sin frameworks pesados)
* **SockJS & Stomp.js** (Cliente para la conexión WebSocket)

### Base de Datos
* **MongoDB** (Almacenamiento de usuarios y mensajes)

## 📸 Capturas de Pantalla

*(Aquí puedes poner imágenes de tu proyecto. Sube las capturas a una carpeta en tu repo y enlázalas aquí)*

![Login Screen](./screenshots/login.png)
![Chat Interface](./screenshots/chat.png)

## 🔧 Instalación y Ejecución

Sigue estos pasos para correr el proyecto en tu máquina local:

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/Fernand-O-band01/null.git](https://github.com/Fernand-O-band01/null.git)
    cd null
    ```

2.  **Configurar MongoDB:**
    Asegúrate de tener MongoDB corriendo localmente o configura la URI en `src/main/resources/application.properties`:
    ```properties
    spring.data.mongodb.uri=mongodb://localhost:27017/null-db
    ```

3.  **Ejecutar el Backend:**
    Si tienes Maven instalado:
    ```bash
    mvn spring-boot:run
    ```

4.  **Acceder a la aplicación:**
    Abre tu navegador y visita: `http://localhost:8080`

## 🧠 Aprendizajes

Durante el desarrollo de **Null**, enfrenté y superé varios desafíos técnicos:
* Configuración correcta del **Broker de Mensajería** en Spring para rutas públicas y privadas.
* Manejo de **IDs únicos en MongoDB** para evitar duplicidad de usuarios.
* Sincronización del estado del cliente (Frontend) con los eventos del servidor.

## 🤝 Contribución

Este es un proyecto educativo, ¡pero cualquier sugerencia es bienvenida! Si tienes ideas para mejorarlo, siéntete libre de abrir un *issue* o enviar un *pull request*.

## ✒️ Autor

* **Fernando Obando** - *Trabajo Inicial* - [Fernand-O-band01](https://github.com/Fernand-O-band01)

---
⌨️ con ❤️ por [Fernando Obando](https://github.com/Fernand-O-band01)
