package entities;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Integer index = Integer.parseInt(state.getName().replaceAll("Не\\s*", ""));
                inputs.add(statesMap.get(index));
            }
            rule.setInputs(inputs);

            Integer index = Integer.parseInt(rule.getOutput().getName().replaceAll("Не\\s*", ""));
            rule.setOutput(statesMap.get(index));
        }
        this.rulesList = rulesList;
    }

    @XmlRootElement
    public static class StatesList {

        private List<State> states;

        public void setStates(List<State> states) {
            this.states = states;
        }

        @XmlElement(name = "fact")
        public List<State> getStates() {
            return states;
        }

        @Override
        public String toString() {
            return "StatesList{" +
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
            return "RulesList{" +
                    "rules=" + rules +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Root{" +
                "statesList=" + statesList +
                ",\n rulesList=" + rulesList +
                '}';
    }
}
