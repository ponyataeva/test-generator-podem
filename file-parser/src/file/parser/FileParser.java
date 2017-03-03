package file.parser;

import format.FilePattern;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Add class description
 */
public class FileParser {

    static final String PATH = "D:\\test.txt";

    public static List<String> readFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH), StandardCharsets.UTF_8);
        List<String> comparedLines = new ArrayList<>();
        Pattern pattern = Pattern.compile(FilePattern.PATTERN);
        for (String line : lines) {
            if (pattern.matcher(line).matches()) {
                comparedLines.add(line);
            }
        }
        return comparedLines;
    }
}
