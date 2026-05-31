package models;

public class InputArgs {
    private String inputFile;
    private String outputFile;

    public InputArgs() {
    }

    public InputArgs(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public static InputArgs parse(String[] args) {
        InputArgs inputArgs = new InputArgs();

        if (args.length < 2) {
            throw new IllegalArgumentException(
                    "Недостаточно аргументов. Использование: -i <inputFile> [-o <outputFile>]"
            );
        }

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-i":
                case "--input":
                    if (i + 1 < args.length) {
                        inputArgs.setInputFile(args[++i]);
                    } else {
                        throw new IllegalArgumentException(
                                "Не указано имя входного файла после " + args[i]
                        );
                    }
                    break;

                case "-o":
                case "--output":
                    if (i + 1 < args.length) {
                        inputArgs.setOutputFile(args[++i]);
                    } else {
                        throw new IllegalArgumentException(
                                "Не указано имя выходного файла после " + args[i]
                        );
                    }
                    break;

                default:
                    System.err.println("Предупреждение: неизвестный аргумент " + args[i]);
            }
        }

        if (inputArgs.getInputFile() == null) {
            throw new IllegalArgumentException("Не указан входной файл (параметр -i)");
        }

        return inputArgs;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public boolean hasOutputFile() {
        return outputFile != null && !outputFile.isEmpty();
    }

    @Override
    public String toString() {
        return "InputArgs{" +
                "inputFile='" + inputFile + '\'' +
                ", outputFile='" + outputFile + '\'' +
                '}';
    }
}
