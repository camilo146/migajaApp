# Etapa de construcción
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Etapa de producción con Nginx
FROM nginx:alpine
# Copiamos los archivos construidos (Angular 17+ con 'application' builder usa /browser)
COPY --from=build /app/dist/migaja-app/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80