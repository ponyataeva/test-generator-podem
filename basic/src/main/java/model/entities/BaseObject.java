package model.entities;

/**
 * Add class description
 */
public class BaseObject {

    String name;
    Integer index;

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

    public Integer getIndex() {
        if (index == null) {
            return Integer.MIN_VALUE;
        }
        return index;
    }
}
