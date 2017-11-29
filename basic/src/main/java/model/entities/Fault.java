package model.entities;

/**
 * Add class description
 */
public class Fault {

    private Fact fact;

    public Fault(Fact fact) {
        this.fact = fact;
    }

    public Fact getFact() {
        return fact;
    }

    public void setFact(Fact fact) {
        this.fact = fact;
    }
}
