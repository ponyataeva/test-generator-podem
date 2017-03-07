package rule.utils;

import entities.Rule;
import entities.State;

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
        Set<State> allStates = new HashSet<>();
        for (Rule rule : rules) {
            allStates.addAll(rule.getStates());
            allStates.add(rule.getAction());
        }
        int[][] result = new int[allStates.size()][allStates.size()];

        for (Rule rule : rules) {
            int destinationIndex = rule.getAction().getIndex();
            for (State state : rule.getStates()) {
                result[state.getIndex()][destinationIndex] = 1;
            }
        }
        return result;
    }
}
