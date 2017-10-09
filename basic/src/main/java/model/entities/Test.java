package model.entities;

import model.entities.impl.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Add class description
 */
public class Test {
    private Map<String, Value> PIValues;
    private String POName;
    private Value POValue;

    public Test() {
        PIValues = new HashMap<>();
    }

    public void addPIs(List<State> states) {
        for (State pi : states) {
            PIValues.put(pi.getName(), pi.getValue());
        }
    }

    public Map<String, Value> getPIValues() {
        return new HashMap<>(PIValues);
    }

    public String getPOName() {
        return POName;
    }

    public void setPOName(String POName) {
        this.POName = POName;
    }

    public Value getPOValue() {
        return POValue;
    }

    public void setPOValue(Value POValue) {
        this.POValue = POValue;
    }

    @Override
    public String toString() {
        return "Test{" +
                "PIValues=" + PIValues +
                ", POName='" + POName + '\'' +
                ", POValue=" + POValue +
                '}';
    }
}
