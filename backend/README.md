# MigajaApp Backend

API REST para la plataforma de historias de relaciones.

## Requisitos

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

## Configuración

1. Crear la base de datos PostgreSQL:
```sql
CREATE DATABASE migajadb;
```

2. Configurar las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/migajadb
spring.datasource.username=postgres
spring.datasource.password=tu_password
```

3. Configurar JWT secret (cambiar en producción):
```properties
jwt.secret=tu_secret_key_segura
```

## Ejecutar

```bash
cd backend
mvn spring-boot:run
```

El servidor se iniciará en http://localhost:8080

## Endpoints Principales

### Autenticación
- POST `/api/auth/register` - Registro de usuario
- POST `/api/auth/login` - Inicio de sesión

### Historias
- GET `/api/stories/public` - Lista de historias publicadas
- POST `/api/stories` - Crear historia
- GET `/api/stories/{id}` - Ver historia
- PUT `/api/stories/{id}` - Actualizar historia
- POST `/api/stories/{id}/publish` - Publicar historia
- DELETE `/api/stories/{id}` - Eliminar historia

### Calificaciones y Comentarios
- POST `/api/stories/{id}/rate` - Calificar historia
- POST `/api/stories/{id}/comments` - Comentar historia
- GET `/api/stories/{id}/comments` - Ver comentarios

## Estructura del Proyecto

```
backend/
├── src/main/java/com/migaja/
│   ├── controller/       # Controladores REST
│   ├── dto/             # DTOs de request/response
│   ├── model/           # Entidades JPA
│   ├── repository/      # Repositorios
│   ├── service/         # Lógica de negocio
│   ├── security/        # Configuración de seguridad JWT
│   └── exception/       # Manejo de excepciones
└── src/main/resources/
    └── application.properties
```

## Stack Tecnológico

- Spring Boot 3.2.1
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Lombok
- Bean Validation
