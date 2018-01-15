package model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Add class description
 */
public class Scheme {

    private Set<Rule> allRules = new HashSet<>();

    private List<Fact> PIs = new ArrayList<>();
    private Fact PO;

    public Scheme(Set<Rule> allRules) {
        this.allRules = allRules;
        for (Rule rule : this.allRules) {
            PIs.addAll(rule.getInputs().stream().filter(Fact::isPrimaryInput).collect(Collectors.toList()));
            if (rule.getOutput().isPrimaryOutput()) {
                PO = rule.getOutput();
            }
            System.out.println(rule + " was success parsed");
        }
        calculateControllability();
    }

    private void calculateControllability() {
        PIs.forEach(this::calculateControllability);
        System.out.println("Controllability of states calculated success");
    }

    private void calculateControllability(Fact fact) {
        fact.isInputFor().forEach(rule -> {
            Fact out = rule.getOutput();
            out.setCC0(rule.calculateCC0());
            out.setCC1(rule.calculateCC1());
        });

        fact.isInputFor().forEach(rule -> calculateControllability(rule.getOutput()));
    }


    public List<Fact> getPIs() {
        return PIs;
    }

    public Fact getPO() {
        return PO;
    }

    public Test getTest() {
        Test test = new Test();
        test.addPIs(getPIs());
        test.setPOName(getPO().getName());
        test.setPOValue(getPO().getValue());
        return test;
    }

    public void clear() {
        allRules.forEach(rule -> {
            rule.getInputs().forEach(Fact::setToDefault);
            rule.getOutput().setToDefault();
        });
    }

    @Override
    public String toString() {
        return "Scheme{" +
                "allRules=" + allRules +
                '}';
    }

    public void print() {
        System.out.println(this);
    }
}
