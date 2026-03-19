package console;

import core.DataProcessor;
import core.FileUtils;
import models.InputArgs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConsoleApp {

    public static void main(String[] args) {
        System.out.println("=== Консольное приложение для обработки данных ===");
        System.out.println("Вариант 17: поиск пары чисел с максимальным произведением");
        System.out.println("Полученные аргументы: " + Arrays.toString(args));
        System.out.println();

        try {
            InputParams params = parseArguments(args);

            System.out.println("Чтение данных из файла: " + params.inputFile);
            List<String> lines = FileUtils.readAllLines(params.inputFile);
            System.out.println("Прочитано строк: " + lines.size());

            System.out.println("Исходные данные:");
            for (int i = 0; i < lines.size(); i++) {
                System.out.println("  " + (i + 1) + ": " + lines.get(i));
            }
            System.out.println();

            System.out.println("Обработка данных...");
            DataProcessor processor = new DataProcessor();

            String[] inputArray = lines.toArray(new String[0]);

            String[] result = processor.processPipeline(inputArray);

            System.out.println("\nРезультаты обработки:");
            if (result.length == 0) {
                System.out.println("  Нет результатов (недостаточно данных или ошибка)");
            } else {
                System.out.println("  Найдена пара чисел с максимальным произведением:");
                System.out.println("  Число 1: " + result[0]);
                System.out.println("  Число 2: " + result[1]);
                System.out.println("  Произведение: " + result[2]);
            }

            if (params.outputFile != null) {
                System.out.println("\nЗапись результатов в файл: " + params.outputFile);
                FileUtils.writeLines(params.outputFile, result);
                System.out.println("Результаты успешно сохранены");
            }

            System.out.println("\nОбработка завершена успешно!");

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка в аргументах: " + e.getMessage());
            printUsage();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static InputParams parseArguments(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Аргументы не указаны");
        }

        InputParams params = new InputParams();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                case "--input":
                    if (i + 1 < args.length) {
                        params.inputFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Не указан входной файл");
                    }
                    break;

                case "-o":
                case "--output":
                    if (i + 1 < args.length) {
                        params.outputFile = args[++i];
                    } else {
                        throw new IllegalArgumentException("Не указан выходной файл");
                    }
                    break;

                default:
                    System.err.println("Предупреждение: игнорируем неизвестный аргумент: " + args[i]);
            }
        }

        if (params.inputFile == null) {
            throw new IllegalArgumentException("Обязательно укажите входной файл (-i)");
        }

        return params;
    }

    private static void printUsage() {
        System.out.println("\nИспользование:");
        System.out.println("  java console.ConsoleApp -i <входной_файл> [-o <выходной_файл>]");
        System.out.println();
        System.out.println("Параметры:");
        System.out.println("  -i, --input   путь к входному файлу с данными (обязательно)");
        System.out.println("  -o, --output  путь к выходному файлу для результатов (опционально)");
        System.out.println();
        System.out.println("Примеры:");
        System.out.println("  java console.ConsoleApp -i test/input01.txt");
        System.out.println("  java console.ConsoleApp -i test/input01.txt -o output/result.txt");
    }

    private static class InputParams {
        String inputFile;
        String outputFile;
    }
}