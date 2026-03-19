package core;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> readAllLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    public static void writeLines(String filePath, String[] lines) throws IOException {
        Path path = Paths.get(filePath);
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int i = 0; i < lines.length; i++) {
                writer.write(lines[i]);
                if (i < lines.length - 1) {
                    writer.newLine();
                }
            }
        }
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
}