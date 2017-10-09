package rule.utils;


import model.entities.Gate;
import model.entities.State;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Екатерина on 30.04.2017.
 */
public class RuleService {

    private static List<Gate> allGates;
    private static RuleService service;

    private RuleService() {
    }

    public static RuleService getInstance() {
        if (service == null) {
            service = new RuleService();
        }
        return service;
    }

    public static void setAllGates(List<Gate> allGates) {
        RuleService.allGates = allGates;
    }

    /**
     * Get all rules contained target state.
     *
     * @param state which should containsInput in rules/
     * @return list of rules.
     */
    public List<Gate> getRulesByInput(State state) {
        List<Gate> gates = new ArrayList<>();
//        return allGates.stream().filter(rule -> rule.containsInput(state)).collect(Collectors.toList());
        for (Gate gate : allGates) {
            if (gate.containsInput(state)) {
                gates.add(gate);
            }
        } return gates;
    }

    /**
     * Get all rules contained target state.
     *
     * @param state which should containsInput in rules/
     * @return list of rules.
     */
    public List<Gate> getRulesByOutput(State state) {
        List<Gate> gates = new ArrayList<>();
//        return allGates.stream().filter(rule -> rule.containsInput(state)).collect(Collectors.toList());
        for (Gate gate : allGates) {
            if (gate.containsOutput(state)) {
                gates.add(gate);
            }
        } return gates;
    }
}
