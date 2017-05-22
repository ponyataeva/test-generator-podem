package entities;

/**
 * Add class description
 */
public class Fault {

    private State state;

    public Fault(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
