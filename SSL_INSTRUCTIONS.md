# Guía de Configuración HTTPS (Candado Seguro)

Sigue estos pasos para asegurar tu aplicación con un certificado SSL gratuito.

## Paso 1: Actualizar la configuración de puertos (En tu PC Local)
Ya hemos cambiado el puerto del frontend en `docker-compose.yml` al 4200 para dejar libre el puerto 80 para el sistema de seguridad.

1.  Abre una terminal en VS Code y sube los cambios:
    ```powershell
    git add .
    git commit -m "Preparar puertos para SSL"
    git push
    ```

## Paso 2: Actualizar el VPS (En la consola de DigitalOcean)
Conéctate a tu VPS y actualiza la aplicación:

1.  Entra al servidor:
    ```bash
    ssh root@104.131.112.150
    ```
2.  Ve a la carpeta y actualiza:
    ```bash
    cd migajaApp
    git pull
    docker compose down
    docker compose up -d --build
    ```
    *(En este punto tu web dejará de funcionar por el puerto 80 momentáneamente. Es normal).*

## Paso 3: Instalar Nginx y Certbot (En el VPS)
Vamos a instalar el servidor web Nginx directamente en el VPS para que maneje la seguridad.

Ejecuta estos comandos uno por uno en el VPS:

```bash
sudo apt update
sudo apt install -y nginx certbot python3-certbot-nginx
```

## Paso 4: Configurar Nginx
Vamos a decirle a Nginx que envíe el tráfico a tu aplicación Docker.

1.  Crea el archivo de configuración:
    ```bash
    nano /etc/nginx/sites-available/migaja
    ```

2.  Pega el siguiente contenido (Usa clic derecho para pegar):
    ```nginx
    server {
        listen 80;
        server_name migajerosbucaramanga.fun;

        location / {
            proxy_pass http://localhost:4200;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
    }
    ```

3.  Guarda y sal:
    *   Presiona `Ctrl + O`, luego `Enter`.
    *   Presiona `Ctrl + X`.

4.  Activa el sitio y reinicia Nginx:
    ```bash
    ln -s /etc/nginx/sites-available/migaja /etc/nginx/sites-enabled/
    rm /etc/nginx/sites-enabled/default
    nginx -t
    systemctl restart nginx
    ```
    *(Ahora tu web debería volver a funcionar en http://migajerosbucaramanga.fun)*

## Paso 5: Activar el Candado (SSL)
Finalmente, ejecutamos el robot de certificados:

```bash
sudo certbot --nginx -d migajerosbucaramanga.fun
```

*   Te pedirá un correo (pon el tuyo).
*   Acepta los términos (Y).
*   Si pregunta sobre redirigir HTTP a HTTPS, elige **2 (Redirect)**.

¡Listo! Tu sitio ahora es seguro.
