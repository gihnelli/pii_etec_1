@echo off
set "LIBS=libs/pdfbox-app-3.0.7.jar"
set "BIN=bin"
set "SRC=src"

echo Compilando LabQuest Desktop...
if not exist "%BIN%" mkdir "%BIN%"

dir /s /b "%SRC%\*.java" > sources.txt
javac -d "%BIN%" -cp "%LIBS%;%SRC%;." @sources.txt
del sources.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO NA COMPILACAO! Verifique as mensagens acima.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Executando LabQuest...
java -cp "%BIN%;%LIBS%;%SRC%;." telas.autenticacao.TelaLogin
pause
