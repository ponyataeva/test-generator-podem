package rule.utils;

import model.entities.Fact;
import model.entities.Gate;

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
        Set<Fact> allFacts = new HashSet<>();
        for (Gate gate : gates) {
            allFacts.addAll(gate.getInputs());
            allFacts.add(gate.getOutput());
        }
        int[][] result = new int[allFacts.size()][allFacts.size()];

        for (Gate gate : gates) {
            int destinationIndex = gate.getOutput().getIndex();
            for (Fact fact : gate.getInputs()) {
                result[fact.getIndex()][destinationIndex] = 1;
            }
        }
        return result;
    }
}
