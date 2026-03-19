@echo off
echo Запуск графического приложения...
echo.
java --module-path "C:\javafx-sdk-21.0.10\lib" --add-modules javafx.controls,javafx.fxml -jar WindowApp.jar
pause