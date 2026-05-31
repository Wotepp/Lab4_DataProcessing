package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataProcessor {

    public List<Double> processData(List<Double> input) {
        List<Double> result = new ArrayList<>();

        if (input == null || input.size() < 2) {
            System.err.println("Ошибка: недостаточно данных для обработки");
            return result;
        }

        double maxProduct = Double.NEGATIVE_INFINITY;
        Double firstNum = null;
        Double secondNum = null;

        for (int i = 0; i < input.size(); i++) {
            for (int j = i + 1; j < input.size(); j++) {
                Double num1 = input.get(i);
                Double num2 = input.get(j);

                if (num1 == null || num2 == null) {
                    continue;
                }

                double product = num1 * num2;

                if (product > maxProduct) {
                    maxProduct = product;
                    firstNum = num1;
                    secondNum = num2;
                }
            }
        }

        if (firstNum != null && secondNum != null) {
            result.add(firstNum);
            result.add(secondNum);
            result.add(maxProduct);
        }

        return result;
    }

    public String[] processPipeline(String[] input) {
        List<Double> numbers = parseStringsToDoubles(input);

        List<Double> result = processData(numbers);

        return convertDoublesToStrings(result);
    }

    private List<Double> parseStringsToDoubles(String[] strings) {
        List<Double> numbers = new ArrayList<>();

        if (strings == null) {
            return numbers;
        }

        for (int i = 0; i < strings.length; i++) {
            String line = strings[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            try {
                Double number = Double.parseDouble(line);
                numbers.add(number);
            } catch (NumberFormatException e) {
                System.err.printf("Ошибка в строке %d: '%s' не является числом (пропущено)%n",
                        i + 1, line);
            }
        }

        return numbers;
    }

    private String[] convertDoublesToStrings(List<Double> doubles) {
        if (doubles == null || doubles.isEmpty()) {
            return new String[0];
        }

        String[] result = new String[doubles.size()];
        for (int i = 0; i < doubles.size(); i++) {
            Double value = doubles.get(i);
            if (value != null) {
                if (value == Math.floor(value)) {
                    result[i] = String.format("%.0f", value);
                } else {
                    result[i] = value.toString();
                }
            } else {
                result[i] = "null";
            }
        }

        return result;
    }
}
