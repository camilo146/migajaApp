@echo off
echo ====================================
echo   INSTALACION - MigajaApp Backend
echo ====================================
echo.

cd backend

echo [1/2] Limpiando proyecto...
call mvnw.cmd clean

echo.
echo [2/2] Instalando dependencias...
call mvnw.cmd install -DskipTests

echo.
echo ====================================
echo   Backend instalado correctamente!
echo ====================================
echo.
echo Para ejecutar: start-backend.bat
pause
