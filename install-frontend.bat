@echo off
echo ====================================
echo   INSTALACION - MigajaApp Frontend
echo ====================================
echo.

cd frontend

echo Instalando dependencias de Node.js...
echo Esto puede tardar varios minutos...
echo.

call npm install

echo.
echo ====================================
echo   Frontend instalado correctamente!
echo ====================================
echo.
echo Para ejecutar: start-frontend.bat
pause
