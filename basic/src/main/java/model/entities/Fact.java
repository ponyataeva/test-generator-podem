package model.entities;

import model.entities.impl.FaultValueImpl;
import model.entities.impl.Value;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * The preconditions of Action type.
 */
public class Fact extends BaseObject implements Comparable {

    private Value value = Value.X;
    private FaultValueImpl faultType = FaultValueImpl.NONE;
    private SortedSet<Gate> isInputFor = new TreeSet<>();
    private SortedSet<Gate> isOutputFor = new TreeSet<>();
    private int CC0 = 1;
    private int CC1 = 1;

    private boolean isNegation;

    private boolean alternateAssignmentTried;

    public Fact() {
    }

    public Fact(String name) {
        this.name = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
        System.out.println("Set [" + name + "] = " + value);
    }

    public boolean isAssigned() {
        return value != Value.X;
    }

    public boolean isUnassigned() {
        return !isAssigned();
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
        return value.equals(Value.D) || value.equals(Value.NOT_D);
    }

    @Override
    public int compareTo(Object o) {
        return index.compareTo(((Fact) o).getIndex());
    }

    public boolean equals(Object fact) {
        if (fact instanceof Fact) {
            return name.equals(((Fact) fact).getName()) && index.equals(((Fact) fact).getIndex());
        } else {
            return super.equals(fact);
        }
    }

    public boolean inversionOf(Object o) {
        return o instanceof Fact && this.name.equals(((Fact) o).getName());
    }

    public int hashCode() {
        return name.hashCode();
    }

    public boolean isNegation() {
        return isNegation;
    }

    public void setNegation(boolean negation) {
        isNegation = negation;
    }

    public String toString() {
        return String.format("(%s)%s %s", index, isNegation ? "(negation)" : "", name);
    }
}
