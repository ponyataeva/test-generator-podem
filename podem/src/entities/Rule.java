package entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Add class description
 */
@XmlRootElement
public class Rule extends XmlBaseObject {

    private List<State> inputs;
    private State output;
    private boolean isNegation;

    @XmlElement(name = "input")
    public List<State> getInputs() {
        return inputs;
    }

    public void setInputs(List<State> inputs) {
        this.inputs = inputs;
    }

    @XmlElement
    public State getOutput() {
        return output;
    }

    public void setOutput(State output) {
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
        return "Rule{" +
                "index=" + index +
                ", inputs=" + inputs +
                ", output=" + output +
                '}';
    }
}
