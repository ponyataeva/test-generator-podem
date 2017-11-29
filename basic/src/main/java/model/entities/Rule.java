package model.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Add class description
 */
@XmlRootElement
public class Rule extends XmlBaseObject {

    private List<Fact> inputs;
    private Fact output;
    private boolean isNegation;

    @XmlElement(name = "input")
    public List<Fact> getInputs() {
        return inputs;
    }

    public void setInputs(List<Fact> inputs) {
        this.inputs = inputs;
    }

    @XmlElement
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
        return "Rule{" +
                "index=" + index +
                ", inputs=" + inputs +
                ", output=" + output +
                '}';
    }
}
