package model.entities.utils;

import model.entities.Fact;

import java.util.HashSet;
import java.util.Set;

/**
 * Utils class for Fact objects.
 */
public class FactUtils {

    private static Set<Fact> allFacts = new HashSet<>();

    /**
     * Multitone for the fact objects.
     * <p>
     * Find by the name object. If object already exists return it.
     * else create new object and set index to it.
     *
     * @param factName - name of the fact
     * @return the fact object with index.
     */
    public static Fact getFact(String factName, int index) {
        Fact resultFact = new Fact(factName);
        for (Fact fact : allFacts) {
            if (fact.equals(resultFact)) {
                return fact;
            }
        }
        resultFact.setIndex(index);
        allFacts.add(resultFact);

        return resultFact;
    }

    public static Fact getFact(String name) {
        return allFacts.stream()
                .filter(fact -> name.equals(fact.getName()))
                .findFirst().get();
    }

    public static Fact getFact(int index) {
        for (Fact fact : allFacts) {
            if (fact.getIndex().equals(index)) {
                return fact;
            }
        }
        return null;
    }

//    public static List<Fact> getInputs(int[][] matrix) {
//        List<Fact> facts = new ArrayList<>();
//        for (int i = 0; i < matrix.length; i++) {
//            if (isInput(matrix, i)) {
//                Fact fact = getFact(i);
//                facts.add(fact);
//            }
//        }
//        return facts;
//    }
//
//    public static List<Fact> getOutputs(int[][] matrix) {
//        List<Fact> facts = new ArrayList<>();
//        for (int i = 0; i < matrix.length; i++) {
//            if (isOutput(matrix[i])) {
//                Fact fact = getFact(i);
//                facts.add(fact);
//            }
//        }
//        return facts;
//    }
//
//    /**
//     * get D-frontier for state
//     *
//     * @param matrix
//     * @param stateIndex
//     * @return
//     */
//    public static List<Fact> getInputs(int[][] matrix, int stateIndex) {
//        List<Fact> facts = new ArrayList<>();
//        for (int i = 0; i < matrix.length; i++) {
//            if (1 == matrix[i][stateIndex]) {
//                Fact fact = getFact(i);
//                facts.add(fact);
//            }
//        }
//        return facts;
//    }
//
//    /**
//     * get J-frontier for state
//     *
//     * @param matrix
//     * @param stateIndex
//     * @return
//     */
//    public static List<Fact> getOutputs(int[][] matrix, int stateIndex) {
//        List<Fact> facts = new ArrayList<>();
//        for (int i = 0; i < matrix.length; i++) {
//            if (1 == matrix[stateIndex][i]) {
//                Fact fact = getFact(i);
//                facts.add(fact);
//            }
//        }
//        return facts;
//    }
//
//    private static boolean isOutput(int[] stateEdges) {
//        for (int stateEdge : stateEdges) {
//            if (1 == stateEdge) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private static boolean isInput(int[][] matrix, int index) {
//        for (int j = 0; j < matrix.length; j++) {
//            if (1 == matrix[j][index]) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public static Set<Fact> getAllFacts() {
//        return allFacts;
//    }
}
