@echo off
echo ====================================
echo   INICIANDO Backend - MigajaApp
echo ====================================
echo.
echo Backend se iniciara en: http://localhost:8080
echo Presiona Ctrl+C para detener
echo.

cd backend
call mvn spring-boot:run
