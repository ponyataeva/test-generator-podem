package model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Add class description
 */
public class Scheme {

    private Set<Gate> allGates = new HashSet<>();

    private List<Fact> PIs = new ArrayList<>();
    private Fact PO;

    public Scheme(Set<Gate> allGates) {
        this.allGates = allGates;
        for (Gate gate : this.allGates) {
            for (Fact input : gate.getInputs()) {
                if (input.isPrimaryInput()) {
                    PIs.add(input);
                }
            }
            if (gate.getOutput().isPrimaryOutput()) {
                PO = gate.getOutput();
            }
            System.out.println(gate + " was success parsed");
        }
        calculateControllability();
    }

    private void calculateControllability() {
        for (Fact pi : PIs) {
            calculateControllability(pi);
        }
        System.out.println("Controllability of states calculated success");
    }

    private void calculateControllability(Fact fact) {
        Fact out;
        for(Gate gate : fact.isInputFor()) {
            out = gate.getOutput();
            out.setCC0(gate.calculateCC0());
            out.setCC1(gate.calculateCC1());
        }

        for(Gate gate : fact.isInputFor()) {
            calculateControllability(gate.getOutput());
        }
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

    @Override
    public String toString() {
        return "Scheme{" +
                "allGates=" + allGates +
                '}';
    }

    public void print(){
        System.out.println(this);
    }
}
