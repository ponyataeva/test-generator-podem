package model.entities.utils;

import model.entities.State;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utils class for State objects.
 */
public class StateUtils {

    private static Set<State> allStates = new HashSet<>();
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
    public static State getState(String stateName) {
        State resultState = new State(stateName);
        for (State state : allStates) {
            if (state.equals(resultState)) {
                return state;
            }
        }
        resultState.setIndex(currentStateIndex);
        allStates.add(resultState);
        currentStateIndex++;

        return resultState;
    }

    public static State getState(int index) {
        for (State state : allStates) {
            if (state.getIndex().equals(index)) {
                return state;
            }
        }
        return null;
    }

    public static List<State> getInputs(int[][] matrix) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isInput(matrix, i)) {
                State state = getState(i);
                states.add(state);
            }
        }
        return states;
    }

    public static List<State> getOutputs(int[][] matrix) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (isOutput(matrix[i])) {
                State state = getState(i);
                states.add(state);
            }
        }
        return states;
    }

    /**
     * get D-frontier for state
     * @param matrix
     * @param stateIndex
     * @return
     */
    public static List<State> getInputs(int[][] matrix, int stateIndex) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (1 == matrix[i][stateIndex]) {
                State state = getState(i);
                states.add(state);
            }
        }
        return states;
    }

    /**
     * get J-frontier for state
     *
     * @param matrix
     * @param stateIndex
     * @return
     */
    public static List<State> getOutputs(int[][] matrix, int stateIndex) {
        List<State> states = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            if (1 == matrix[stateIndex][i]) {
                State state = getState(i);
                states.add(state);
            }
        }
        return states;
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

    public static Set<State> getAllStates() {
        return allStates;
    }
}
