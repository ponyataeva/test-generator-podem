package rules.parser.parse;


//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Add class description
 */
@Deprecated
public class FileReader {

    private static final String PATH = "test_rules.xlsx";
    private static final String FILE = "test.txt";
    private static final String RULES_SHEET = "Rules";

//    public static List<String> readFile() throws IOException {
//        List<String> lines = Files.readAllLines(Paths.get(FILE), StandardCharsets.UTF_8);
//        List<String> comparedLines = new ArrayList<>();
//        Pattern pattern = Pattern.compile(RulePattern.PATTERN);
//        for (String line : lines) {
//            if (pattern.matcher(line).matches()) {
//                comparedLines.add(line);
//            }
//        }
//        return comparedLines;
//    }

//    public static List<Gate> readExcel() throws IOException {
//        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(PATH));
//        XSSFSheet rulesSheet = workbook.getSheet(RULES_SHEET);
//        List<Gate> rules = new ArrayList<>();
//
//        for (int i = 1; i <= rulesSheet.getLastRowNum(); i++) {
//            XSSFRow row = rulesSheet.getRow(i);
//            Set<Fact> preconditions = new HashSet<>();
//            for (int j = 2; j < row.getLastCellNum(); j++) {
//                XSSFCell cell = row.getCell(j);
//                String cellValue = getCellValue(cell);
//                if (!THEN.equalsIgnoreCase(cellValue)) {
//                    preconditions.add(FactUtils.createState(cellValue));
//                } else {
//                    cell = row.getCell(j + 1);
//                    Fact action = FactUtils.createState(getCellValue(cell));
//                    rules.add(new Gate(preconditions, action));
//                    break;
//                }
//            }
//        }
//        return rules;
//    }
//
//    private static String getCellValue(XSSFCell cell) {
//        int type = cell.getCellType();
//        if (type == 0) {
//            return String.valueOf(cell.getNumericCellValue());
//        } else {
//            return cell.getStringCellValue();
//        }
//    }


}
