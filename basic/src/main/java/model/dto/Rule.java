package model.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Екатерина on 03.12.2017.
 */
@XmlRootElement
public class Rule extends XmlBaseObject {

    private List<Fact> inputs;
    private Fact output;

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

    @Override
    public String toString() {
        return "Rule{" +
                "index=" + getId() +
                ", inputs=" + inputs +
                ", output=" + output +
                '}';
    }
}
