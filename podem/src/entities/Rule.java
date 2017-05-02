package entities;

import java.util.Set;

/**
 * Consists preconditions and action.
 */
public class Rule {

    private Set<State> states;
    private State action;

    public Rule(Set<State> states, State action) {
        this.states = states;
        this.action = action;
    }

    public Set<State> getStates() {
        return states;
    }

    public State getAction() {
        return action;
    }

    public boolean contains(State state) {
        for (State ruleState : states) {
            if (ruleState.equals(state)) {
                return true;
            }
        } return false;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "states = " + states +
                ", action : " + action +
                '}';
    }
}
