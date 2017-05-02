package entities;

/**
 * The preconditions or Action type.
 */
public class State {

    private Integer index;
    private String name;
    private Alphabet symbol;

    public State(String name) {
        this.name = name;
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

    public Alphabet getSymbol() {
        return symbol;
    }

    public void setSymbol(Alphabet symbol) {
        this.symbol = symbol;
    }

    public boolean isAssigned() {
        return symbol == null;
    }

    public String toString() {
        return index + " = " + name;
    }
}
