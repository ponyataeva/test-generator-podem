package model.entities;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * Add class description
 */
public class XmlBaseObject {

    String name;

    @XmlAttribute(name = "id")
    Integer index;

    @XmlValue()
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "XmlState{" +
                "name='" + name + '\'' +
                '}';
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @XmlTransient()
    public Integer getIndex() {
        if (index == null) {
            return Integer.MIN_VALUE;
        }
        return index;
    }
}
