package model.entities.utils;

import model.entities.Fact;
import model.entities.Gate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Add class description
 */
public class GateUtils {

    private static Set<Gate> allGates = new HashSet<>();
    private static AtomicInteger sequence = new AtomicInteger();

    public static Gate getGate(SortedSet<Fact> inputs, Fact output) {
        return allGates.stream()
                .filter(gate -> gate.getOutput().equals(output))
                .peek(gate -> gate.addInputs(inputs))
                .peek(gate -> inputs.forEach(input -> input.addIsInputFor(gate)))
                .findFirst().orElse(createNewGate(inputs, output));
    }

    private static Gate createNewGate(SortedSet<Fact> inputs, Fact output) {
        Gate resultGate = new Gate(inputs, output);
        resultGate.setIndex(sequence.incrementAndGet());
        allGates.add(resultGate);

        output.addIsOutputFor(resultGate);
        inputs.forEach(input -> input.addIsInputFor(resultGate));

        return resultGate;
    }
}
