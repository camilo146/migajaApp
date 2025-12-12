# 🎭 MigajaApp

Plataforma moderna para compartir historias de relaciones con calificaciones, comentarios y torneos.

## 📋 Descripción del Proyecto

MigajaApp es una aplicación web full-stack que permite a los usuarios:
- 📝 Compartir sus historias de relaciones con fotos y evidencias
- ⭐ Calificar y comentar historias de otros usuarios
- 🏆 Participar en torneos con premios
- 👥 Crear perfiles de usuario personalizados
- 🛡️ Panel de administración para moderación de contenido

## 🚀 Stack Tecnológico

### Backend
- **Java 17** con Spring Boot 3.2.1
- **Spring Security** + JWT para autenticación
- **Spring Data JPA** para persistencia
- **PostgreSQL** como base de datos
- **Lombok** para reducir boilerplate
- **Bean Validation** para validaciones

### Frontend
- **Angular 17+** (Standalone Components)
- **TypeScript** 5.2
- **SCSS** con variables CSS modernas
- **RxJS** para programación reactiva
- **Angular Material** (opcional)
- Diseño **responsive** y **mobile-first**

## 📁 Estructura del Proyecto

```
migajaApp/
├── backend/                 # API REST con Spring Boot
│   ├── src/main/java/com/migaja/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Lógica de negocio
│   │   ├── repository/     # Repositorios JPA
│   │   ├── model/          # Entidades
│   │   ├── dto/            # DTOs
│   │   ├── security/       # Configuración JWT
│   │   └── exception/      # Manejo de errores
│   ├── pom.xml
│   └── README.md
│
└── frontend/               # App Angular
    ├── src/app/
    │   ├── core/          # Servicios, guards, interceptors
    │   ├── features/      # Componentes por funcionalidad
    │   └── shared/        # Componentes compartidos
    ├── package.json
    └── README.md
```

## 🛠️ Instalación y Configuración

### Prerrequisitos
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Maven 3.8+

### 1. Configurar Base de Datos

```sql
CREATE DATABASE migajadb;
```

### 2. Configurar Backend

```bash
cd backend

# Editar src/main/resources/application.properties
# Configurar credenciales de PostgreSQL

# Ejecutar
mvn spring-boot:run
```

El backend se ejecutará en `http://localhost:8080`

### 3. Configurar Frontend

```bash
cd frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm start
```

El frontend se ejecutará en `http://localhost:4200`

## 🎨 Características de Diseño

### Diseño Moderno y Limpio
- ✨ Paleta de colores vibrante (Indigo + Pink)
- 🎯 Interfaz minimalista y fácil de usar
- 📱 Totalmente responsive
- 🌈 Gradientes y sombras sutiles
- ⚡ Animaciones suaves y transiciones
- 🔤 Tipografía Poppins

### Componentes Clave
- **Hero Section**: Página de inicio impactante
- **Cards modernas**: Para historias y torneos
- **Formularios elegantes**: Con validación en tiempo real
- **Sistema de calificaciones**: Estrellas interactivas
- **Comentarios**: Sistema de comentarios en tiempo real

## 📊 Modelo de Datos

### Entidades Principales
- **User**: Usuarios del sistema (USER/ADMIN)
- **Story**: Historias de relaciones
- **Photo**: Fotos de las historias
- **Rating**: Calificaciones (1-5 estrellas)
- **Comment**: Comentarios
- **Tournament**: Torneos
- **TournamentEntry**: Inscripciones a torneos
- **Notification**: Notificaciones del sistema

## 🔐 Seguridad

- Autenticación basada en **JWT**
- Contraseñas hasheadas con **BCrypt**
- Roles de usuario (USER, ADMIN)
- Guards en el frontend para rutas protegidas
- CORS configurado
- Validación de datos en backend y frontend

## 🏆 Sistema de Torneos

- Inscripción con pago de $10,000
- Premio de $100,000
- Sistema de votación
- Ranking automático
- Gestión por administradores

## 👨‍💼 Panel de Administración

- Gestión de usuarios
- Moderación de contenido
- Eliminar publicaciones con notificación
- Dashboard de estadísticas
- Gestión de torneos

## 🚀 Despliegue a Producción

### Backend
1. Configurar base de datos en producción
2. Actualizar `application.properties`
3. Generar JAR: `mvn clean package`
4. Ejecutar: `java -jar target/migaja-app-1.0.0.jar`

### Frontend
1. Build: `npm run build`
2. Archivos en `dist/migaja-app/`
3. Desplegar en servidor web (Nginx, Apache)
4. Configurar variables de entorno

### Recomendaciones
- Usar HTTPS en producción
- Configurar variables de entorno
- Habilitar compresión gzip
- Usar CDN para assets estáticos
- Configurar cache de navegador
- Monitoreo con logs

## 📝 API Endpoints

### Autenticación
- `POST /api/auth/register` - Registro
- `POST /api/auth/login` - Login

### Historias
- `GET /api/stories/public` - Listar historias
- `GET /api/stories/{id}` - Ver historia
- `POST /api/stories` - Crear historia
- `PUT /api/stories/{id}` - Actualizar
- `DELETE /api/stories/{id}` - Eliminar
- `POST /api/stories/{id}/publish` - Publicar

### Calificaciones y Comentarios
- `POST /api/stories/{id}/rate` - Calificar
- `POST /api/stories/{id}/comments` - Comentar
- `GET /api/stories/{id}/comments` - Ver comentarios

## 🤝 Contribución

Este es un proyecto privado. Para contribuir:
1. Fork el proyecto
2. Crear rama: `git checkout -b feature/nueva-funcionalidad`
3. Commit: `git commit -m 'Agregar funcionalidad'`
4. Push: `git push origin feature/nueva-funcionalidad`
5. Abrir Pull Request

## 📄 Licencia

Proyecto privado - Todos los derechos reservados



**¡Comparte tu historia y conecta con otros! 💕**
