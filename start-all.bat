@echo off
echo ====================================
echo   INICIO COMPLETO - MigajaApp
echo ====================================
echo.
echo Este script iniciara:
echo 1. Backend en http://localhost:8080
echo 2. Frontend en http://localhost:4200
echo.
echo Presiona cualquier tecla para continuar...
pause > nul

echo.
echo Iniciando Backend...
start cmd /k "cd backend && mvnw.cmd spring-boot:run"

timeout /t 5 > nul

echo Iniciando Frontend...
start cmd /k "cd frontend && npm start"

echo.
echo ====================================
echo   Servidores iniciados!
echo ====================================
echo.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:4200
echo.
echo Cierra las ventanas de terminal para detener los servidores.
pause
