package model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * Add class description
 */
public class XmlBaseObject {

    private String name;

    @XmlAttribute
    private Integer id;

    @XmlValue()
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @XmlTransient()
    public Integer getId() {
        if (id == null) {
            return Integer.MIN_VALUE;
        }
        return id;
    }

    @Override
    public String toString() {
        return "XmlState{" +
                "name='" + name + '\'' +
                '}';
    }
}
