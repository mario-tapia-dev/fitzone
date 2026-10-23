# Fitzone API

API backend desarrollada en **Spring Boot** para la gestión integral de un gimnasio (**FitZone**). Permite administrar socios, membresías, clases, inscripciones y recordatorios de forma eficiente.

---

## 🚀 Características Principales

* **Gestión de Socios:** Control y registro de los miembros del gimnasio (`SocioController`, `SocioService`).
* **Control de Membresías:** Administración de los planes y tipos de suscripción disponibles (`MembresiaController`, `MembresiaService`).
* **Gestión de Clases:** Creación, programación y administración de las actividades y disciplinas impartidas (`ClaseController`, `ClaseService`).
* **Inscripciones:** Registro de socios a las distintas clases y control de cupos (`InscripcionService`).
* **Recordatorios:** Lógica de negocio para notificaciones o alertas a los usuarios (`RecordatorioService`).

---

## 🛠️ Tecnologías Utilizadas

* **Java** (versión compatible con Spring Boot configurada en el proyecto)
* **Spring Boot**
* **Maven** (incluye Maven Wrapper `mvnw`)

---

## ⚙️ Requisitos Previos

Antes de ejecutar la aplicación de forma local, asegúrate de contar con lo siguiente:

1. **Java JDK** instalado (versión 17 o superior recomendada).
2. Un gestor de base de datos configurado (según los parámetros definidos en tu archivo `application.properties`).

---

## 📥 Instrucciones de Ejecución Local

1. **Clona o descarga** este repositorio en tu máquina local.
2. Abre una terminal y navega hasta el directorio raíz del proyecto (`fitzone/fitzone`).
3. Configura tus credenciales y conexión a la base de datos dentro del archivo:
   `src/main/resources/application.properties` (o mediante el archivo `.env` si aplica).
4. Ejecuta la aplicación utilizando el **Maven Wrapper** incluido:

   * **En Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```

   * **En Windows (CMD / PowerShell):
     ```cmd
     mvnw.cmd spring-boot:run
     ```

5. Una vez iniciada, la API estará disponible localmente en el puerto configurado (por defecto `http://localhost:8080`).
