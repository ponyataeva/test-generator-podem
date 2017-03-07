import entities.Rule;
import entities.State;
import entities.StateUtils;
import file.parser.FileReader;
import rule.utils.ArrayHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        List<Rule> rules = FileReader.readExcel();
        int[][] rulesArray = ArrayHelper.toArray(rules);
        List<State> inputs = StateUtils.getInputs(rulesArray);
        List<State> outputs = StateUtils.getOutputs(rulesArray);

        for (int[] res : rulesArray) {
            System.out.println(Arrays.toString(res));
        }
        System.out.println(Arrays.toString(inputs.toArray()));
        System.out.println(Arrays.toString(outputs.toArray()));
    }
}
