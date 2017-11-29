package model.entities;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Add class description
 */
@XmlRootElement
public class Root {

    private FactsList factsList;
    private RulesList rulesList;

    private Map<Integer, Fact> statesMap = new HashMap<>();

    @XmlElement(name = "facts")
    public FactsList getFactsList() {
        return factsList;
    }

    public List<Fact> getStates() {
        return factsList.getFacts();
    }

    public void setFactsList(FactsList factsList) {
        this.factsList = factsList;

        for (Fact fact : factsList.getFacts()) {
            statesMap.put(fact.getIndex(), fact);
        }
    }

    @XmlElement(name = "rules")
    public RulesList getRulesList() {
        return rulesList;
    }

    public void setRulesList(RulesList rulesList) {
        for (Rule rule : rulesList.getRules()) {
            List<Fact> inputs = new ArrayList<>();
            for (Fact fact : rule.getInputs()) {
                Integer index = Integer.parseInt(fact.getName());
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
    public static class FactsList {

        private List<Fact> facts;

        public void setFacts(List<Fact> facts) {
            this.facts = facts;
        }

        @XmlElement(name = "fact")
        List<Fact> getFacts() {
            return facts;
        }

        @Override
        public String toString() {
            return "\nFactsList{" +
                    "facts=" + facts +
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
                "factsList=" + factsList +
                "\nrulesList=" + rulesList +
                '}';
    }
}
