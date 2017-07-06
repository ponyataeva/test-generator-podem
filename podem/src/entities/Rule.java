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

    @Override
    public String toString() {
        return "\n Rule{" +
                "index=" + index +
                ", inputs=" + inputs +
                ", output=" + output +
                '}';
    }
}
