package line.utils;

import entities.Rule;
import entities.State;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Add class description
 */
public class ArrayHelper {

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
