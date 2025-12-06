# MigajaApp Frontend

Aplicación web Angular moderna para compartir historias de relaciones.

## Requisitos

- Node.js 18+ 
- npm 9+
- Angular CLI 17+

## Instalación

```bash
cd frontend
npm install
```

## Desarrollo

```bash
npm start
```

La aplicación se ejecutará en http://localhost:4200

## Build para Producción

```bash
npm run build
```

Los archivos compilados estarán en `dist/migaja-app`

## Estructura del Proyecto

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/              # Servicios, guards, interceptors
│   │   │   ├── guards/        # Guards de autenticación
│   │   │   ├── interceptors/  # HTTP interceptors
│   │   │   ├── models/        # Interfaces TypeScript
│   │   │   └── services/      # Servicios compartidos
│   │   │
│   │   ├── features/          # Módulos de funcionalidades
│   │   │   ├── auth/          # Login, Register
│   │   │   ├── home/          # Página principal
│   │   │   ├── stories/       # Lista, detalle, crear historias
│   │   │   ├── tournaments/   # Sistema de torneos
│   │   │   ├── profile/       # Perfil de usuario
│   │   │   └── admin/         # Panel de administración
│   │   │
│   │   ├── shared/            # Componentes compartidos
│   │   │   ├── components/    # Botones, cards, modales
│   │   │   └── pipes/         # Pipes personalizados
│   │   │
│   │   ├── app.component.ts
│   │   └── app.routes.ts      # Configuración de rutas
│   │
│   ├── assets/                # Imágenes, iconos
│   ├── styles.scss            # Estilos globales
│   └── index.html
│
├── angular.json
├── package.json
└── tsconfig.json
```

## Características

### Diseño Moderno
- UI limpia y minimalista
- Totalmente responsive (mobile-first)
- Animaciones suaves
- Paleta de colores moderna
- Tipografía Poppins

### Funcionalidades
- ✅ Autenticación con JWT
- ✅ CRUD de historias
- ✅ Sistema de calificaciones (estrellas)
- ✅ Comentarios en historias
- ✅ Torneos de migajeros
- ✅ Panel de administración
- ✅ Perfil de usuario
- ✅ Subida de fotos

### Tecnologías
- Angular 17+ (Standalone Components)
- TypeScript
- SCSS
- RxJS
- Angular Material (opcional)
- HttpClient

## Configuración de API

El endpoint de la API se configura en los servicios:
```typescript
private readonly API_URL = 'http://localhost:8080/api';
```

Para producción, cambiar a tu URL de backend.

## Estilos

Los estilos usan variables CSS para fácil personalización:
```scss
:root {
  --primary-color: #6366f1;
  --secondary-color: #ec4899;
  --success-color: #10b981;
  --error-color: #ef4444;
  // ... más variables
}
```

## Responsive Breakpoints

- Mobile: < 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px
