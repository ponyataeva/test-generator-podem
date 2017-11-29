package model.entities.utils;

import model.entities.Fact;
import model.entities.Gate;

import java.util.*;

/**
 * Add class description
 */
public class GateUtils {

    private static Set<Gate> allGates = new HashSet<>();
    private static int currentGateIndex = 0;

    public static Gate getGate(SortedSet<Fact> inputs, Fact output) {
        for (Gate gate : allGates) {
            if (gate.getOutput().equals(output)) {
                gate.addInputs(inputs);
                addIsInputFor(inputs, gate);
                return gate;
            }
        }

        Gate resultGate = new Gate(inputs, output);
        resultGate.setIndex(currentGateIndex);
        allGates.add(resultGate);

        output.addIsOutputFor(resultGate);
        addIsInputFor(inputs, resultGate);
        currentGateIndex++;

        return resultGate;
    }


    public static Gate getGate(List<Fact> inputs, Fact output) {
        SortedSet<Fact> set = new TreeSet<>(inputs);
        for (Gate gate : allGates) {
            if (gate.getOutput().equals(output)) {
                gate.addInputs(set);
                addIsInputFor(set, gate);
                return gate;
            }
        }

        Gate resultGate = new Gate(set, output);
        resultGate.setIndex(currentGateIndex);
        allGates.add(resultGate);

        output.addIsOutputFor(resultGate);
        addIsInputFor(set, resultGate);
        currentGateIndex++;

        return resultGate;
    }

    private static void addIsInputFor(Set<Fact> inputs, Gate gate) {
        for (Fact input : inputs) {
            input.addIsInputFor(gate);
        }
    }

//    public static Set<Gate> getAllGates() {
//        return allGates;
//    }
}
