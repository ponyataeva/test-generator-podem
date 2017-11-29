package model.entities.utils;

import model.entities.Fact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils class for Fact objects.
 */
public class StateUtils {

    private static Set<Fact> allFacts = new HashSet<>();
    private static int currentStateIndex = 0;

    /**
     * Multitone for the state objects.
     * <p>
     * Find by the name object. If object already exists return it.
     * else create new object and set index to it.
     *
     * @param stateName - name of the state
     * @return the state object with setting index.
     */
    public static Fact getState(String stateName) {
        Fact resultFact = new Fact(stateName);
        for (Fact fact : allFacts) {
            if (fact.equals(resultFact)) {
                return fact;
            }
        }
        resultFact.setIndex(currentStateIndex);
        allFacts.add(resultFact);
        currentStateIndex++;

        return resultFact;
    }

    public static Fact getState(int index) {
        for (Fact fact : allFacts) {
            if (fact.getIndex().equals(index)) {
                return fact;
            }
        }
        return null;
    }

    public static List<Fact> getInputs(int[][] matrix) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isInput(matrix, i)) {
                Fact fact = getState(i);
                facts.add(fact);
            }
        }
        return facts;
    }

    public static List<Fact> getOutputs(int[][] matrix) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isOutput(matrix[i])) {
                Fact fact = getState(i);
                facts.add(fact);
            }
        }
        return facts;
    }

    /**
     * get D-frontier for state
     * @param matrix
     * @param stateIndex
     * @return
     */
    public static List<Fact> getInputs(int[][] matrix, int stateIndex) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (1 == matrix[i][stateIndex]) {
                Fact fact = getState(i);
                facts.add(fact);
            }
        }
        return facts;
    }

    /**
     * get J-frontier for state
     *
     * @param matrix
     * @param stateIndex
     * @return
     */
    public static List<Fact> getOutputs(int[][] matrix, int stateIndex) {
        List<Fact> facts = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (1 == matrix[stateIndex][i]) {
                Fact fact = getState(i);
                facts.add(fact);
            }
        }
        return facts;
    }

    private static boolean isOutput(int[] stateEdges) {
        for (int stateEdge : stateEdges) {
            if (1 == stateEdge) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInput(int[][] matrix, int index) {
        for (int j = 0; j < matrix.length; j++) {
            if (1 == matrix[j][index]) {
                return false;
            }
        }
        return true;
    }

    public static Set<Fact> getAllFacts() {
        return allFacts;
    }
}
