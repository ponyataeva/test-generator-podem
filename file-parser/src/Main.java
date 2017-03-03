import com.sun.deploy.util.ArrayUtil;
import file.parser.FileParser;
import line.utils.ArrayHelper;
import line.utils.LineHelper;
import entities.Rule;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Add class description
 */
public class Main {

    public static void main(String[] args) throws IOException {
//        String s = "R1: Если \"A\" и \"C\" и\"D\", то \"B\"";
//        String s2 = "R1: Если \"A\", то \"B\"";
//        List<String> ss = LineHelper.getPreconditions(s);
//        String dest = LineHelper.getAction(s);
//        System.out.println(ArrayUtil.arrayToString(ss.toArray(new String[]{})));
//        System.out.println(dest);

        List<String> lines = FileParser.readFile();
        List<Rule> rules = LineHelper.parse(lines);
        for (int[] res : ArrayHelper.toArray(rules)) {
            System.out.println(ArrayUtil.arrayToString(new String[]{Arrays.toString(res)}));
        }
    }
}
