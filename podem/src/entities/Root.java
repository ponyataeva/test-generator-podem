package entities;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Add class description
 */
@XmlRootElement
public class Root {

    private StatesList statesList;
    private RulesList rulesList;

    private Map<Integer, State> statesMap = new HashMap<>();

    @XmlElement(name = "facts")
    public StatesList getStatesList() {
        return statesList;
    }

    public List<State> getStates() {
        return statesList.getStates();
    }

    public void setStatesList(StatesList statesList) {
        this.statesList = statesList;

        for (State state : statesList.getStates()) {
            statesMap.put(state.getIndex(), state);
        }
    }

    @XmlElement(name = "rules")
    public RulesList getRulesList() {
        return rulesList;
    }

    public void setRulesList(RulesList rulesList) {
        for (Rule rule : rulesList.getRules()) {
            List<State> inputs = new ArrayList<>();
            for (State state : rule.getInputs()) {
                Integer index = Integer.parseInt(state.getName());
                inputs.add(statesMap.get(index));
            }
            rule.setInputs(inputs);
            rule.setNegation(rule.getOutput().isNegation());

            Integer index = Integer.parseInt(rule.getOutput().getName());
            rule.setOutput(statesMap.get(index));
        }
        this.rulesList = rulesList;
    }

    public List<Rule> getRules() {
        return rulesList.getRules();
    }

    @XmlRootElement
    public static class StatesList {

        private List<State> states;

        public void setStates(List<State> states) {
            this.states = states;
        }

        @XmlElement(name = "fact")
        List<State> getStates() {
            return states;
        }

        @Override
        public String toString() {
            return "\nStatesList{" +
                    "states=" + states +
                    '}';
        }
    }

    @XmlRootElement
    public static class RulesList {

        private List<Rule> rules;

        public List<Rule> getRules() {
            return rules;
        }

        @XmlElement(name = "rule")
        public void setRules(List<Rule> rules) {
            this.rules = rules;
        }

        @Override
        public String toString() {
            return "\nRulesList{" +
                    "rules=" + rules +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Root{" +
                "statesList=" + statesList +
                "\nrulesList=" + rulesList +
                '}';
    }
}
