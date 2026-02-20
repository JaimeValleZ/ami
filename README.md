# AMI - Agenda Médica Integral 🏥✨
AMI es una solución tecnológica diseñada para transformar la gestión de datos en el sector salud. No es solo un software de citas; es un aliado estratégico desarrollado para que hospitales y clínicas gestionen su información médica y administrativa de manera segura, ágil y confiable.

## 🚀 Nuestra Misión
Creemos que la tecnología debe estar al servicio de la salud. Nuestro propósito es simplificar la gestión de la información, reducir riesgos de seguridad, optimizar procesos internos y, sobre todo, elevar la calidad de la atención al paciente.

## 🛠️ Stack Tecnológico
Hemos seleccionado herramientas robustas y escalables para garantizar un rendimiento óptimo tanto en la nube como en entornos locales:

#### Backend: Java con Spring Boot (Seguridad y robustez).

#### Frontend: Angular (Interfaz dinámica y profesional).

#### Base de Datos: MySQL (Relacional y confiable).

#### Migraciones: Flyway (Control de versiones de base de datos automatizado).

#### Arquitectura: Implementación escalable lista para entornos de alta demanda.

## 👥 Roles y Funcionalidades
El sistema está diseñado con una estructura de permisos granular para garantizar la seguridad de la información:

## 👤 Pacientes
Agendamiento: Selección de especialistas y horarios disponibles.

#### Visualización: Historial de citas programadas y pasadas.

#### Cancelación: Gestión autónoma de sus espacios médicos.

## 🩺 Médicos
Gestión de Agenda: Visualización en tiempo real de su panel de citas.

#### Acciones: Capacidad para aceptar o cancelar citas según disponibilidad y urgencias.

#### Control de flujo: Organización eficiente de la jornada laboral.

## 🔑 Administrador
Gestión de Talento: Bloqueo y desbloqueo de cuentas de médicos (por licencias, vacaciones o procesos administrativos).

#### Supervisión: Control total sobre la integridad de la base de usuarios.

## 🔧 Configuración e Instalación
Requisitos previos
Java 17 o superior.

Spring Boot.

Node.js (v16+) y Angular CLI.

MySQL Server.

Maven.

### 1. Clonar el repositorio
```
git clone https://github.com/tu-usuario/ami-agenda-medica.git
cd ami-agenda-medica
```
#### 2. Backend (Spring Boot)
Configura tu application.properties con las credenciales de MySQL. Flyway se encargará de crear las tablas automáticamente al iniciar.

```
cd backend
mvn clean install
mvn spring-boot:run
```
#### 3. Frontend (Angular)
```
cd frontend
npm install
ng serve
```
Accede a http://localhost:4200 para ver la aplicación en acción.

## 🛡️ Seguridad y Datos
Gracias a la implementación de Flyway, aseguramos que cada cambio en la estructura de datos sea trazable y reversible. Además, la lógica de negocio en Spring Boot garantiza que los datos sensibles de los pacientes permanezcan protegidos bajo estándares de seguridad modernos.

## 🤝 Contribución y Equipo
Este proyecto es desarrollado por un equipo de profesionales comprometidos con la innovación tecnológica en salud.

#### "Más que un proveedor de software, somos un aliado estratégico de innovación."
