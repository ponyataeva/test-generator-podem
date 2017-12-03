package model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by Екатерина on 03.12.2017.
 */
@XmlRootElement
public class Fact extends XmlBaseObject {

    @XmlAttribute
    private boolean isNegation;

    @XmlTransient
    public boolean isNegation() {
        return isNegation;
    }

    public void setNegation(boolean negation) {
        isNegation = negation;
    }
}
