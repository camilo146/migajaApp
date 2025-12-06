# 🚀 Guía de Inicio Rápido - MigajaApp

Esta guía te ayudará a tener el proyecto funcionando en minutos.

## ⚡ Inicio Rápido

### Paso 1: Instalar PostgreSQL
Si no tienes PostgreSQL instalado:
- Windows: https://www.postgresql.org/download/windows/
- Crear base de datos:
```sql
CREATE DATABASE migajadb;
```

### Paso 2: Configurar Backend

```bash
# Ir a la carpeta backend
cd backend

# Editar application.properties (opcional)
# Cambiar credenciales de PostgreSQL si es necesario:
# spring.datasource.username=tu_usuario
# spring.datasource.password=tu_password

# Ejecutar con Maven (Windows)
mvnw.cmd spring-boot:run

# O si tienes Maven instalado
mvn spring-boot:run
```

✅ Backend funcionando en: http://localhost:8080

### Paso 3: Configurar Frontend

```bash
# Abrir NUEVA terminal
# Ir a la carpeta frontend
cd frontend

# Instalar dependencias
npm install

# Ejecutar aplicación
npm start
```

✅ Frontend funcionando en: http://localhost:4200

## 📝 Probar la Aplicación

### 1. Abrir Navegador
Ir a: http://localhost:4200

### 2. Crear Cuenta
- Clic en "Únete Ahora" o "Registrarse"
- Llenar formulario
- Iniciar sesión

### 3. Crear Primera Historia
- Clic en "Crear Historia"
- Llenar título, contenido y fecha
- Guardar como borrador o publicar

### 4. Explorar Funcionalidades
- Ver historias de otros
- Calificar con estrellas
- Dejar comentarios
- Ver torneos

## 🔧 Solución de Problemas

### Backend no inicia
```
Error: Could not connect to database
```
**Solución**: Verificar que PostgreSQL esté corriendo y las credenciales sean correctas.

### Frontend no compila
```
Error: Cannot find module '@angular/...'
```
**Solución**: 
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
```

### Puerto 8080 ocupado
**Solución**: Cambiar puerto en `backend/src/main/resources/application.properties`:
```properties
server.port=8081
```

### Puerto 4200 ocupado
**Solución**: Usar otro puerto:
```bash
ng serve --port 4300
```

## 🎯 Credenciales por Defecto

El sistema no tiene credenciales por defecto. Debes crear tu usuario al registrarte.

### Crear Usuario Administrador
Para tener acceso al panel admin, modifica manualmente en la base de datos:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'tu_usuario';
```

## 📚 Documentación Completa

- [README Principal](./README.md) - Información completa del proyecto
- [Backend README](./backend/README.md) - Detalles del backend
- [Frontend README](./frontend/README.md) - Detalles del frontend

## 🛠️ Comandos Útiles

### Backend
```bash
# Compilar sin ejecutar
mvn clean package

# Ejecutar tests
mvn test

# Limpiar y compilar
mvn clean install
```

### Frontend
```bash
# Ejecutar en modo desarrollo
npm start

# Build para producción
npm run build

# Ejecutar tests
npm test

# Ver la aplicación en otro puerto
ng serve --port 4300
```

## 🎨 Personalización Rápida

### Cambiar Colores del Frontend
Editar `frontend/src/styles.scss`:
```scss
:root {
  --primary-color: #6366f1;    // Tu color primario
  --secondary-color: #ec4899;   // Tu color secundario
}
```

### Cambiar Puerto del Backend
Editar `backend/src/main/resources/application.properties`:
```properties
server.port=8080  // Cambiar a tu puerto
```

### Cambiar URL de la API en Frontend
Editar cada servicio en `frontend/src/app/core/services/`:
```typescript
private readonly API_URL = 'http://localhost:8080/api';
```

## 📞 ¿Necesitas Ayuda?

Si tienes problemas:
1. Revisar los logs del backend en la terminal
2. Revisar la consola del navegador (F12) para errores del frontend
3. Verificar que ambos servidores estén corriendo
4. Asegurarte de que PostgreSQL esté activo

## 🎉 ¡Listo!

Ya tienes MigajaApp funcionando. Ahora puedes:
- ✅ Crear usuarios
- ✅ Publicar historias
- ✅ Calificar y comentar
- ✅ Administrar contenido (como admin)
- ✅ Crear torneos

---

**¡Disfruta desarrollando MigajaApp! 💕**
