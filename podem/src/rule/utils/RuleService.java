package rule.utils;

import entities.Rule;
import entities.State;

import java.util.List;
import java.util.stream.Collectors;

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
     * Get all rules contained target state.
     *
     * @param state which should contains in rules/
     * @return list of rules.
     */
    public List<Rule> getRulesByState(State state) {
        return allRules.stream().filter(rule -> rule.contains(state)).collect(Collectors.toList());
    }
}
