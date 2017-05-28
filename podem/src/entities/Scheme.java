package entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Add class description
 */
public class Scheme {

    private Set<Gate> allGates = new HashSet<>();

    private List<State> PIs = new ArrayList<>();
    private State PO;

    public Scheme(Set<Gate> allGates) {
        this.allGates = allGates;
        for (Gate gate : this.allGates) {
            for (State input : gate.getInputs()) {
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
        for (State pi : PIs) {
            calculateControllability(pi);
        }
        System.out.println("Controllability of states calculated success");
    }

    public void calculateControllability(State state) {
        State out;
        for(Gate gate : state.isInputFor()) {
            out = gate.getOutput();
            out.setCC0(gate.calculateCC0());
            out.setCC1(gate.calculateCC1());
        }

        for(Gate gate : state.isInputFor()) {
            calculateControllability(gate.getOutput());
        }
    }


    public List<State> getPIs() {
        return PIs;
    }

    public State getPO() {
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
