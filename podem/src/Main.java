import entities.Gate;
import entities.Scheme;
import entities.impl.FaultValueImpl;
import entities.utils.StateUtils;
import parser.FileReader;
import rule.utils.TextHelper;

import java.io.IOException;
import java.util.Set;


public class Main {

    public static void main(String[] args) throws IOException {
//        System.out.println(X.not());

//        List<Gate> rules = FileReader.readExcel();
//        int[][] rulesArray = rule.utils.ArrayHelper.toArray(rules);
//        List<State> inputs = StateUtils.getD_Frontier(rulesArray);
//        List<State> outputs = StateUtils.getJ_Frontier(rulesArray);
//        State state = StateUtils.createState("B");
//        for (int[] res : rulesArray) {
//            System.out.println(Arrays.toString(res));
//        }
//        System.out.println(Arrays.toString(inputs.toArray()));
//        System.out.println(Arrays.toString(outputs.toArray()));

//        System.out.println(StateUtils.getD_Frontier(rulesArray, state.getIndex()));
//        System.out.println(StateUtils.getJ_Frontier(rulesArray, state.getIndex()));
//        RuleService ruleService = RuleService.getInstance();
        Set<Gate> gates = TextHelper.parse(FileReader.readFile());
        Scheme scheme = new Scheme(gates);
//        System.out.println(scheme.toString());

        StateUtils.getState("K").setFaultType(FaultValueImpl.sa0);
//        StateUtils.getState("G").setValue(Value.NOT_D);
        PodemExecutor executer = new PodemExecutor(scheme, StateUtils.getState("K"));
//        System.out.println(StateUtils.getAllStates());
        executer.execute();
        System.out.println(scheme.getTest());
    }
}