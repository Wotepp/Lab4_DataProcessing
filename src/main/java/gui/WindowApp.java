package gui;

import core.DataProcessor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class WindowApp extends Application {

    private DataProcessor processor;
    private TextArea inputArea;
    private ListView<String> resultList;
    private Label statusLabel;
    private Label infoLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        processor = new DataProcessor();
        System.out.println("WindowApp инициализирован");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Обработчик данных - Вариант 17 (Максимальное произведение пары чисел)");
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(500);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #f5f5f5;");

        VBox topPanel = createTopPanel();
        root.setTop(topPanel);

        SplitPane centerPane = createCenterPane();
        root.setCenter(centerPane);

        VBox bottomPanel = createBottomPanel(primaryStage);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        try {
            String css = getClass().getResource("/gui/style.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            }
        } catch (Exception e) {
            System.out.println("Файл стилей не найден, используются стандартные стили");
        }

        primaryStage.show();

        infoLabel.setText("Нажмите 'Загрузить из файла' для выбора файла с числами");

        System.out.println("WindowApp запущен");
    }

    private VBox createTopPanel() {
        VBox topPanel = new VBox(5);
        topPanel.setPadding(new Insets(0, 0, 10, 0));

        Label titleLabel = new Label("Поиск пары чисел с максимальным произведением");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        infoLabel = new Label("Загрузите файл с числами или введите их вручную");
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #7f8c8d;");

        topPanel.getChildren().addAll(titleLabel, infoLabel);
        return topPanel;
    }

    private SplitPane createCenterPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.5);

        VBox leftPanel = new VBox(5);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label inputLabel = new Label("Входные данные:");
        inputLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        inputArea = new TextArea();
        inputArea.setPromptText("Введите числа, каждое на новой строке...\nИли загрузите файл с числами");
        inputArea.setWrapText(true);
        inputArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        Label lineCountLabel = new Label("Строк: 0");
        lineCountLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        inputArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                lineCountLabel.setText("Строк: 0");
            } else {
                int lines = newValue.split("\n").length;
                lineCountLabel.setText("Строк: " + lines);
            }
        });

        leftPanel.getChildren().addAll(inputLabel, inputArea, lineCountLabel);
        VBox.setVgrow(inputArea, Priority.ALWAYS);

        VBox rightPanel = new VBox(5);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label resultLabel = new Label("Результаты обработки:");
        resultLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #34495e;");

        resultList = new ListView<>();
        resultList.setPlaceholder(new Label("Результаты появятся здесь после обработки..."));
        resultList.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13px;");

        rightPanel.getChildren().addAll(resultLabel, resultList);
        VBox.setVgrow(resultList, Priority.ALWAYS);

        splitPane.getItems().addAll(leftPanel, rightPanel);
        return splitPane;
    }

    private VBox createBottomPanel(Stage stage) {
        VBox bottomPanel = new VBox(5);
        bottomPanel.setPadding(new Insets(10, 0, 0, 0));

        HBox buttonPanel = new HBox(10);
        buttonPanel.setPadding(new Insets(5, 0, 5, 0));
        buttonPanel.setStyle("-fx-alignment: center;");

        Button processButton = new Button("Обработать данные");
        processButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        processButton.setPrefWidth(150);
        processButton.setOnAction(e -> processData());

        Button loadButton = new Button("Загрузить из файла");
        loadButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-cursor: hand;");
        loadButton.setPrefWidth(150);
        loadButton.setOnAction(e -> loadFromFile(stage));

        Button saveButton = new Button("Сохранить результат");
        saveButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
        saveButton.setPrefWidth(150);
        saveButton.setOnAction(e -> saveToFile(stage));

        Button clearButton = new Button("Очистить всё");
        clearButton.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-cursor: hand;");
        clearButton.setPrefWidth(150);
        clearButton.setOnAction(e -> clearAll());

        Button exitButton = new Button("Выход");
        exitButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-cursor: hand;");
        exitButton.setPrefWidth(100);
        exitButton.setOnAction(e -> exitApplication());

        buttonPanel.getChildren().addAll(
                processButton, loadButton, saveButton, clearButton, exitButton
        );

        statusLabel = new Label("Готов к работе. Загрузите файл с числами из папки test/");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-style: italic; -fx-padding: 5; -fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-radius: 3;");

        bottomPanel.getChildren().addAll(buttonPanel, statusLabel);
        return bottomPanel;
    }

    private void loadFromFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файл с числами");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        File testDir = new File("test");
        if (testDir.exists()) {
            fileChooser.setInitialDirectory(testDir);
        } else {
            boolean created = testDir.mkdirs();
            if (created) {
                System.out.println("Создана папка test");
            }
        }

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                System.out.println("Выбран файл: " + selectedFile.getAbsolutePath());
                System.out.println("Размер файла: " + selectedFile.length() + " байт");

                List<String> lines = Files.readAllLines(selectedFile.toPath(), java.nio.charset.StandardCharsets.UTF_8);

                List<String> nonEmptyLines = new ArrayList<>();
                for (String line : lines) {
                    String trimmed = line.trim();
                    if (!trimmed.isEmpty()) {
                        nonEmptyLines.add(trimmed);
                    }
                }

                if (nonEmptyLines.isEmpty()) {
                    showAlert("Ошибка", "Файл не содержит данных");
                    return;
                }

                StringBuilder content = new StringBuilder();
                for (String line : nonEmptyLines) {
                    content.append(line).append("\n");
                }

                inputArea.setText(content.toString().trim());
                statusLabel.setText("Загружен файл: " + selectedFile.getName() +
                        " (" + nonEmptyLines.size() + " чисел)");
                infoLabel.setText("Файл загружен. Нажмите 'Обработать данные'");

                System.out.println("Загружены числа:");
                for (int i = 0; i < Math.min(5, nonEmptyLines.size()); i++) {
                    System.out.println("  " + nonEmptyLines.get(i));
                }
                if (nonEmptyLines.size() > 5) {
                    System.out.println("  ... и ещё " + (nonEmptyLines.size() - 5));
                }

            } catch (java.nio.charset.MalformedInputException e) {
                System.err.println("Ошибка кодировки файла: " + e.getMessage());
                showAlert("Ошибка", "Файл имеет неправильную кодировку. Попробуйте сохранить файл в UTF-8.");

            } catch (IOException e) {
                System.err.println("Ошибка ввода/вывода: " + e.getMessage());
                e.printStackTrace();
                showAlert("Ошибка", "Не удалось загрузить файл: " + e.getMessage());
                statusLabel.setText("Ошибка загрузки файла");
            }
        }
    }

    private void saveToFile(Stage stage) {
        if (resultList.getItems().isEmpty()) {
            showAlert("Предупреждение", "Нет результатов для сохранения");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить результаты");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt")
        );
        fileChooser.setInitialFileName("result.txt");

        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        fileChooser.setInitialDirectory(outputDir);

        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try {
                List<String> lines = new ArrayList<>(resultList.getItems());
                Files.write(selectedFile.toPath(), lines);
                statusLabel.setText("Результаты сохранены в: " + selectedFile.getName());
            } catch (IOException e) {
                showAlert("Ошибка", "Не удалось сохранить файл: " + e.getMessage());
                statusLabel.setText("Ошибка сохранения файла");
            }
        }
    }

    private void processData() {
        try {
            String input = inputArea.getText();

            if (input == null || input.trim().isEmpty()) {
                showAlert("Ошибка", "Введите данные для обработки или загрузите файл");
                statusLabel.setText("Ошибка: нет данных для обработки");
                infoLabel.setText("Введите числа в поле слева или загрузите файл");
                return;
            }

            String[] lines = input.split("\\n");

            List<String> nonEmptyLines = new ArrayList<>();
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    nonEmptyLines.add(line);
                }
            }

            if (nonEmptyLines.isEmpty()) {
                showAlert("Ошибка", "Нет данных для обработки (все строки пустые)");
                statusLabel.setText("Ошибка: нет данных");
                return;
            }

            statusLabel.setText("Обработка данных... (" + nonEmptyLines.size() + " строк)");
            infoLabel.setText("Обработка...");

            String[] result = processor.processPipeline(nonEmptyLines.toArray(new String[0]));

            displayResults(result, nonEmptyLines.size());

            statusLabel.setText("Обработка завершена. Найдена пара с максимальным произведением");
            infoLabel.setText("Готово. Можно ввести новые данные или загрузить другой файл");

        } catch (Exception e) {
            statusLabel.setText("Ошибка при обработке: " + e.getMessage());
            showAlert("Ошибка", "Не удалось обработать данные: " + e.getMessage());
        }
    }

    private void displayResults(String[] result, int inputSize) {
        ObservableList<String> items = FXCollections.observableArrayList();

        if (result == null || result.length == 0) {
            items.add("НЕ УДАЛОСЬ НАЙТИ ПАРУ ЧИСЕЛ");
            items.add("");
            items.add("Возможные причины:");
            items.add("• В файле меньше 2 чисел (сейчас: " + inputSize + ")");
            items.add("• Все строки содержат некорректные числа");
            items.add("• Ошибка в формате данных");
        } else {
            items.add("РЕЗУЛЬТАТ ОБРАБОТКИ (Вариант 17)");
            items.add("═══════════════════════════════");
            items.add("");
            items.add("Найдена пара чисел с МАКСИМАЛЬНЫМ произведением:");
            items.add("");
            items.add("   Первое число:  " + result[0]);
            items.add("   Второе число:  " + result[1]);
            items.add("   Произведение:  " + result[2]);
            items.add("");
            items.add("Детали:");
            items.add("   • Всего обработано чисел: " + inputSize);
            items.add("   • Проверено пар чисел: " + (inputSize * (inputSize - 1) / 2));
        }

        resultList.setItems(items);
    }

    private void clearAll() {
        inputArea.clear();
        resultList.getItems().clear();
        statusLabel.setText("Готов к работе. Загрузите файл с числами из папки test/");
        infoLabel.setText("Загрузите файл с числами или введите их вручную");
    }

    private void exitApplication() {
        System.out.println("WindowApp завершает работу");
        Platform.exit();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void stop() {
        System.out.println("WindowApp остановлен");
    }
}
