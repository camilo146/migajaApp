# Guía para Configurar Dominio y HTTPS (SSL)

Sigue estos pasos una vez que hayas comprado tu dominio y configurado los registros DNS (Tipo A) apuntando a la IP de tu servidor.

## 1. Modificar Nginx para tu Dominio

Necesitamos actualizar la configuración de Nginx en tu proyecto para que reconozca tu dominio.

1.  En tu PC (VS Code), abre el archivo `frontend/nginx.conf`.
2.  Cambia `server_name localhost;` por tu dominio real.
    Ejemplo: `server_name migajaapp.com www.migajaapp.com;`
3.  Guarda el archivo, haz commit y push a GitHub:
    ```powershell
    git add .
    git commit -m "Configurar dominio en nginx"
    git push
    ```
4.  En el servidor, actualiza el código y reinicia:
    ```bash
    cd ~/migajaApp
    git pull
    docker compose up --build -d frontend
    ```

## 2. Configurar HTTPS (El candadito verde) - Método Fácil con Nginx Proxy Manager

La forma más sencilla de manejar SSL en Docker sin complicarse con archivos de configuración manuales es usar **Nginx Proxy Manager**. Sin embargo, como ya tenemos un Nginx configurado dentro del contenedor, usaremos un enfoque directo con **Certbot** en el servidor host (la máquina Ubuntu).

### Paso A: Instalar Nginx en el servidor (Host)
Usaremos el Nginx del servidor Ubuntu como "puerta de entrada" (Reverse Proxy) que manejará el HTTPS y le pasará el tráfico a tu Docker.

En la terminal del servidor (SSH):

```bash
# 1. Detener el contenedor frontend actual para liberar el puerto 80
docker compose stop frontend

# 2. Instalar Nginx y Certbot en Ubuntu
apt-get update
apt-get install -y nginx certbot python3-certbot-nginx
```

### Paso B: Configurar el Proxy Inverso
Vamos a decirle al Nginx de Ubuntu que envíe todo el tráfico a tu contenedor Docker (que correrá en otro puerto, ej. 8080 o interno).

1.  Edita `docker-compose.yml` en el servidor (o en tu PC y sube cambios):
    Cambia los puertos del frontend para que NO use el 80 directamente, sino uno interno (ej. 8081).
    ```yaml
    frontend:
      ports:
        - "8081:80"  # Cambiado de "80:80" a "8081:80"
    ```
    *(Recuerda hacer `git pull` y `docker compose up -d` si cambias esto).*

2.  Configura Nginx en Ubuntu:
    ```bash
    nano /etc/nginx/sites-available/migajaapp
    ```
    Pega esto (cambia `tu-dominio.com`):
    ```nginx
    server {
        server_name tu-dominio.com www.tu-dominio.com;

        location / {
            proxy_pass http://localhost:8081; # Apunta al puerto de tu Docker Frontend
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
        }
    }
    ```

3.  Activa el sitio:
    ```bash
    ln -s /etc/nginx/sites-available/migajaapp /etc/nginx/sites-enabled/
    rm /etc/nginx/sites-enabled/default  # Borra el default si existe
    nginx -t  # Prueba que todo esté bien
    systemctl restart nginx
    ```

### Paso C: Obtener el Certificado SSL
Ahora la magia de Certbot:

```bash
certbot --nginx -d tu-dominio.com -d www.tu-dominio.com
```
Sigue las instrucciones (pon tu email, acepta términos). Certbot configurará automáticamente el HTTPS.

¡Listo! Tu sitio ahora debería cargar con `https://`.
