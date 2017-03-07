package file.parser;


import entities.Rule;
import entities.State;
import entities.StateUtils;
import rule.utils.RulePattern;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static rule.utils.RulePattern.THEN;

/**
 * Add class description
 */
public class FileReader {

    private static final String PATH = "test_rules.xlsx";
    private static final String RULES_SHEET = "Rules";

    public static List<String> readFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(PATH), StandardCharsets.UTF_8);
        List<String> comparedLines = new ArrayList<>();
        Pattern pattern = Pattern.compile(RulePattern.PATTERN);
        for (String line : lines) {
            if (pattern.matcher(line).matches()) {
                comparedLines.add(line);
            }
        }
        return comparedLines;
    }

    public static List<Rule> readExcel() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(PATH));
        XSSFSheet rulesSheet = workbook.getSheet(RULES_SHEET);
        List<Rule> rules = new ArrayList<>();

        for (int i = 1; i <= rulesSheet.getLastRowNum(); i++) {
            XSSFRow row = rulesSheet.getRow(i);
            Set<State> preconditions = new HashSet<>();
            for (int j = 2; j < row.getLastCellNum(); j++) {
                XSSFCell cell = row.getCell(j);
                String cellValue = cell.getStringCellValue();
                if (!THEN.equalsIgnoreCase(cellValue)) {
                    preconditions.add(StateUtils.getState(cellValue));
                } else {
                    cell = row.getCell(j + 1);
                    State action = StateUtils.getState(cell.getStringCellValue());
                    rules.add(new Rule(preconditions, action));
                    break;
                }
            }
        }
        return rules;
    }


}
