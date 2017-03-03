package entities;

import java.util.Set;

/**
 *
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
}
