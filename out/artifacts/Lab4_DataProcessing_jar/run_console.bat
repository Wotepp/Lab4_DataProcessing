@echo off
echo Запуск консольного приложения...
echo.
echo Создаём тестовый файл...
mkdir test 2>nul
echo 1 > test\input01.txt
echo 10 >> test\input01.txt
echo 2 >> test\input01.txt
echo 6 >> test\input01.txt
echo 5 >> test\input01.txt
echo 3 >> test\input01.txt
echo -4 >> test\input01.txt
echo 8 >> test\input01.txt
echo -2 >> test\input01.txt
echo 7 >> test\input01.txt
echo.
echo Запускаем обработку...
java -jar Lab4_DataProcessing.jar -i test\input01.txt -o output\result.txt
echo.
pause