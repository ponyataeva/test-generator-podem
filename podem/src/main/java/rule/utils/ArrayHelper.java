package rule.utils;

import model.entities.Gate;
import model.entities.State;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for Array of rules.
 */
public class ArrayHelper {

    /**
     * Build by list of gates the adjacency matrix.
     * 0 - if an edge not exists,
     * 1 - an edge exists.
     *
     * @param gates - list with gates.
     * @return adjacency matrix
     */
    public static int[][] toArray(List<Gate> gates) {
        Set<State> allStates = new HashSet<>();
        for (Gate gate : gates) {
            allStates.addAll(gate.getInputs());
            allStates.add(gate.getOutput());
        }
        int[][] result = new int[allStates.size()][allStates.size()];

        for (Gate gate : gates) {
            int destinationIndex = gate.getOutput().getIndex();
            for (State state : gate.getInputs()) {
                result[state.getIndex()][destinationIndex] = 1;
            }
        }
        return result;
    }
}
