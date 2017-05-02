import entities.StateUtils;
import parser.FileReader;
import rule.utils.RuleService;
import rule.utils.TextHelper;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println(X.not());

//        List<Rule> rules = FileReader.readExcel();
//        int[][] rulesArray = rule.utils.ArrayHelper.toArray(rules);
//        List<State> inputs = StateUtils.getInputs(rulesArray);
//        List<State> outputs = StateUtils.getOutputs(rulesArray);
//        State state = StateUtils.getState("B");
//        for (int[] res : rulesArray) {
//            System.out.println(Arrays.toString(res));
//        }
//        System.out.println(Arrays.toString(inputs.toArray()));
//        System.out.println(Arrays.toString(outputs.toArray()));

//        System.out.println(StateUtils.getInputs(rulesArray, state.getIndex()));
//        System.out.println(StateUtils.getOutputs(rulesArray, state.getIndex()));
        RuleService ruleService = RuleService.getInstance();
        RuleService.setAllRules(TextHelper.parse(FileReader.readFile()));

        ruleService.getRulesByState(StateUtils.getState("A")).forEach(System.out::println);

//        System.out.println(ONE.and(D).and(NOT_D).and(ONE));
    }
}
