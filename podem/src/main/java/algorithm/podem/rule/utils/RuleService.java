package algorithm.podem.rule.utils;


import model.entities.Rule;
import model.entities.Fact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Екатерина on 30.04.2017.
 */
public class RuleService {

    private static List<Rule> allRules;
    private static RuleService service;

    private RuleService() {
    }

    public static RuleService getInstance() {
        if (service == null) {
            service = new RuleService();
        }
        return service;
    }

    public static void setAllRules(List<Rule> allRules) {
        RuleService.allRules = allRules;
    }

    /**
     * Get all rules contained target fact.
     *
     * @param fact which should containsInput in rules/
     * @return list of rules.
     */
    public List<Rule> getRulesByInput(Fact fact) {
        List<Rule> rules = new ArrayList<>();
//        return allRules.stream().filter(rule -> rule.containsInput(fact)).collect(Collectors.toList());
        for (Rule rule : allRules) {
            if (rule.containsInput(fact)) {
                rules.add(rule);
            }
        } return rules;
    }

    /**
     * Get all rules contained target fact.
     *
     * @param fact which should containsInput in rules/
     * @return list of rules.
     */
    public List<Rule> getRulesByOutput(Fact fact) {
        List<Rule> rules = new ArrayList<>();
//        return allRules.stream().filter(rule -> rule.containsInput(fact)).collect(Collectors.toList());
        for (Rule rule : allRules) {
            if (rule.containsOutput(fact)) {
                rules.add(rule);
            }
        } return rules;
    }
}
