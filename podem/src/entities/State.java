package entities;

import entities.impl.FaultValueImpl;
import entities.impl.Value;

import java.util.*;

import static entities.impl.Value.*;

/**
 * The preconditions or Action type.
 */
public class State implements Comparable {

    private Integer index;
    private String name;
    private Value value = X;
    private FaultValueImpl faultType = FaultValueImpl.NONE;
    private SortedSet<Gate> isInputFor = new TreeSet<>();
    private SortedSet<Gate> isOutputFor = new TreeSet<>();
    private int CC0 = 1;
    private int CC1 = 1;

    private boolean alternateAssignmentTried;

    public State(String name) {
        this.name = name;
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

    public String getName() {
        return name;
    }

    public boolean equals(Object rule) {
        if (rule instanceof State) {
            return name.equals(((State) rule).getName());
        } else {
            return super.equals(rule);
        }
    }

    public int hashCode() {
        return name.hashCode();
    }

    public Value getValue() {
        return value;
    }

    public Value getValue(List<State> pPath) {
        if (pPath.contains(this)) {
            // TODO refactor it PLS!
            if (value.equals(ONE)) {
                return D;
            } else if (value.equals(ZERO)) {
                return NOT_D;
            }
        }
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
        System.out.println("Set [" + name + "] = " + value);
    }

    public boolean isAssigned() {
        return value != X;
    }

    public boolean isUnassigned() {
        return !isAssigned();
    }

    public String toString() {
        return "\n" + name + " {index = " + index + " , " + "value = " + value + " , CC0=" + CC0 + " , CC1=" + CC1 + "}";
    }

    public SortedSet<Gate> isInputFor() {
        return isInputFor;
    }

    public void addIsInputFor(Gate gate) {
        this.isInputFor.add(gate);
    }

    public Set<Gate> isOutputFor() {
        return isOutputFor;
    }

    public void addIsOutputFor(Gate gate) {
        this.isOutputFor.add(gate);
    }

    public FaultValueImpl getFaultType() {
        return faultType;
    }

    public void setFaultType(FaultValueImpl faultType) {
        this.faultType = faultType;
    }

    public boolean isPrimaryInput() {
        return isOutputFor.isEmpty();
    }

    public boolean isPrimaryOutput() {
        return isInputFor.isEmpty();
    }

    public boolean hasControllingValue(Operation operation) {
        return value.equals(operation.getControllingValue());
    }

    public int getCC0() {
        return CC0;
    }

    public void setCC0(int CC0) {
        this.CC0 = CC0;
    }

    public int getCC1() {
        return CC1;
    }

    public void setCC1(int CC1) {
        this.CC1 = CC1;
    }

    public void setAlternateAssignmentTried(boolean isTried) {
        alternateAssignmentTried = isTried;
    }

    public boolean isAlternateAssignment() {
        return alternateAssignmentTried;
    }

    public boolean hasDisagreementValue() {
        return value.equals(D) || value.equals(NOT_D);
    }

    @Override
    public int compareTo(Object o) {
        return index.compareTo(((State) o).getIndex());
    }
}
