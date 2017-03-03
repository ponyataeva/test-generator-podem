package entities;

/**
 * Add class description
 */
public class State {

    private Integer index;
    private String name;

    public State(String state) {
        this.name = state;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object rule) {
        if (rule instanceof State) {
            return name.equals(((State) rule).getName());
        } else {
            return super.equals(rule);
        }
    }

    public int hashCode() {
        return name.hashCode();
    }
}
