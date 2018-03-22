package model.entities;

import java.util.List;

/**
 * Add class description
 */
public class RuleOld extends BaseObject {

    private List<Fact> inputs;
    private Fact output;
    private boolean isNegation;

    public List<Fact> getInputs() {
        return inputs;
    }

    public void setInputs(List<Fact> inputs) {
        this.inputs = inputs;
    }

    public Fact getOutput() {
        return output;
    }

    public void setOutput(Fact output) {
        this.output = output;
    }

    public boolean isNegation() {
        return isNegation;
    }

    public void setNegation(boolean negation) {
        isNegation = negation;
    }

    @Override
    public String toString() {
        return "RuleOld{" +
                "index=" + index +
                ", inputs=" + inputs +
                ", output=" + output +
                '}';
    }
}
