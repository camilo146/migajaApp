# Guía de Despliegue en DigitalOcean (VPS)

Esta guía te llevará paso a paso para desplegar **MigajaApp** en un servidor Ubuntu de DigitalOcean.

## 1. Preparar el Servidor (Droplet)

1.  Entra a tu cuenta de DigitalOcean.
2.  Crea un nuevo **Droplet**.
    *   **Imagen:** Ubuntu 24.04 (LTS) o 22.04.
    *   **Plan:** Basic -> Regular -> **$6/mo** (1GB RAM, 1 CPU) es suficiente para empezar.
    *   **Autenticación:** Crea una contraseña segura (o usa llave SSH si sabes cómo).
3.  Una vez creado, copia la **Dirección IP** (ej. `123.45.67.89`).

## 2. Conectarse al Servidor

Abre tu terminal en Windows (PowerShell) y ejecuta:

```powershell
ssh root@TU_DIRECCION_IP
```
*(Escribe "yes" si te pregunta por la autenticidad y luego pon la contraseña que creaste).*

## 3. Instalar Docker en el Servidor

Copia y pega estos comandos en la terminal del servidor (uno por uno o en bloque):

```bash
# Actualizar sistema
apt-get update && apt-get upgrade -y

# Instalar Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# Instalar Docker Compose (si no vino con el script anterior)
apt-get install -y docker-compose-plugin
```

## 4. Subir tu Proyecto

Tienes dos opciones. La más fácil si no usas Git es copiar los archivos desde tu PC.

**Opción A: Copiar archivos desde tu PC (Recomendada si no tienes el código en GitHub)**

1.  Abre **otra** terminal de PowerShell en tu PC (en la carpeta del proyecto `migajaApp`).
2.  Ejecuta este comando para copiar todo al servidor (reemplaza la IP):

```powershell
scp -r backend frontend docker-compose.yml root@TU_DIRECCION_IP:/root/migajaApp
```

**Opción B: Usar Git (Si ya subiste el código a GitHub)**

En el servidor ejecuta:
```bash
git clone https://github.com/TU_USUARIO/migajaApp.git
cd migajaApp
```

## 5. Configurar Variables de Entorno (Seguridad)

En el servidor, entra a la carpeta y crea un archivo `.env` para guardar tu contraseña segura.

```bash
cd /root/migajaApp
nano .env
```

Pega lo siguiente (cambia la contraseña por una difícil):

```env
DB_PASSWORD=TuContrasenaSuperSegura123!
```

Presiona `Ctrl+O`, `Enter` para guardar, y `Ctrl+X` para salir.

## 6. Iniciar la Aplicación

Ejecuta el comando mágico:

```bash
docker compose up --build -d
```

¡Listo! Espera unos minutos a que se construya todo.
Ahora puedes entrar a `http://TU_DIRECCION_IP` en tu navegador y verás tu aplicación funcionando.

---

## (Opcional) Configurar Dominio y HTTPS

Si compraste un dominio (ej. `migajaapp.com`):

1.  En el panel de tu dominio, crea un registro **A** que apunte a la IP de tu servidor.
2.  Para tener el candadito verde (HTTPS), necesitarás configurar **Certbot** con Nginx en el servidor. Esto es un paso avanzado, pero por ahora tu app ya funciona en la web.
