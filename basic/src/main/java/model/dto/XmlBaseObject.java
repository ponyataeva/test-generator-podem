package model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 * Add class description
 */
@XmlRootElement
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
            return -1;
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
