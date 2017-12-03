package model.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@XmlRootElement
public class Root {

    private FactsList factsList;
    private RulesList rulesList;

    @XmlElement(name = "facts")
    public FactsList getFactsList() {
        return factsList;
    }

    public void setFactsList(FactsList factsList) {
        this.factsList = factsList;
    }

    @XmlElement(name = "rules")
    public RulesList getRulesList() {
        return rulesList;
    }

    public void setRulesList(RulesList rulesList) {
        this.rulesList = rulesList;
    }

    public List<Rule> getRules() {
        return rulesList.getRules();
    }

    @XmlRootElement
    public static class FactsList {

        private List<XmlBaseObject> facts;

        public void setFacts(List<XmlBaseObject> facts) {
            this.facts = facts;
        }

        @XmlElement(name = "fact")
        List<XmlBaseObject> getFacts() {
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
