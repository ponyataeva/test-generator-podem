package rule.utils;


import model.entities.Gate;
import model.entities.Fact;

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
     * Get all rules contained target fact.
     *
     * @param fact which should containsInput in rules/
     * @return list of rules.
     */
    public List<Gate> getRulesByInput(Fact fact) {
        List<Gate> gates = new ArrayList<>();
//        return allGates.stream().filter(rule -> rule.containsInput(fact)).collect(Collectors.toList());
        for (Gate gate : allGates) {
            if (gate.containsInput(fact)) {
                gates.add(gate);
            }
        } return gates;
    }

    /**
     * Get all rules contained target fact.
     *
     * @param fact which should containsInput in rules/
     * @return list of rules.
     */
    public List<Gate> getRulesByOutput(Fact fact) {
        List<Gate> gates = new ArrayList<>();
//        return allGates.stream().filter(rule -> rule.containsInput(fact)).collect(Collectors.toList());
        for (Gate gate : allGates) {
            if (gate.containsOutput(fact)) {
                gates.add(gate);
            }
        } return gates;
    }
}
