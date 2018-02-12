package algorithm.podem.rule.utils;

import model.entities.Fact;
import model.entities.Rule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Helper class for Array of rules.
 */
public class ArrayHelper {

    /**
     * Build by list of rules the adjacency matrix.
     * 0 - if an edge not exists,
     * 1 - an edge exists.
     *
     * @param rules - list with rules.
     * @return adjacency matrix
     */
    public static int[][] toArray(List<Rule> rules) {
        Set<Fact> allFacts = new HashSet<>();
        for (Rule rule : rules) {
            allFacts.addAll(rule.getInputs());
            allFacts.add(rule.getOutput());
        }
        int[][] result = new int[allFacts.size()][allFacts.size()];

        for (Rule rule : rules) {
            int destinationIndex = rule.getOutput().getIndex();
            for (Fact fact : rule.getInputs()) {
                result[fact.getIndex()][destinationIndex] = 1;
            }
        }
        return result;
    }
}
