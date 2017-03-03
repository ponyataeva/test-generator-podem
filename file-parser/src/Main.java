import com.sun.deploy.util.ArrayUtil;
import file.parser.FileParser;
import format.Condition;
import format.GraphUtils;
import format.LineParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Add class description
 */
public class Main {

    public static void main(String[] args) throws IOException {
//        String s = "R1: Если \"A\" и \"C\" и\"D\", то \"B\"";
//        String s2 = "R1: Если \"A\", то \"B\"";
//        List<String> ss = LineParser.getConditionsList(s);
//        String dest = LineParser.getDestination(s);
//        System.out.println(ArrayUtil.arrayToString(ss.toArray(new String[]{})));
//        System.out.println(dest);

        List<String> lines = FileParser.readFile();
        Map<Set<Condition>, Condition> conditionMap = LineParser.parse(lines);
        for (int[] res : GraphUtils.toMatrix(conditionMap)) {
            System.out.println(ArrayUtil.arrayToString(new String[]{Arrays.toString(res)}));
        }
    }
}
