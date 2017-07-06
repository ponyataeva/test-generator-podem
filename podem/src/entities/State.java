package entities;

import entities.impl.FaultValueImpl;
import entities.impl.Value;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static entities.impl.Value.*;

/**
 * The preconditions or Action type.
 */
@XmlRootElement
public class State extends XmlBaseObject implements Comparable {

    private Value value = X;
    private FaultValueImpl faultType = FaultValueImpl.NONE;
    private SortedSet<Gate> isInputFor = new TreeSet<>();
    private SortedSet<Gate> isOutputFor = new TreeSet<>();
    private int CC0 = 1;
    private int CC1 = 1;

    private boolean alternateAssignmentTried;

    public State() {
    }

    public State(String name) {
        this.name = name;
    }

    @XmlTransient
    public Value getValue() {
        return value;
    }

//    public Value getValue(List<State> pPath) {
//        if (pPath.contains(this)) {
//            // TODO refactor it PLS!
//            if (value.equals(ONE)) {
//                return D;
//            } else if (value.equals(ZERO)) {
//                return NOT_D;
//            }
//        }
//        return value;
//    }

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
        return "\n" + name + " {index = " + index + "}";
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


    @XmlTransient
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

//    public boolean hasControllingValue(Operation operation) {
//        return value.equals(operation.getControllingValue());
//    }


    @XmlTransient
    public int getCC0() {
        return CC0;
    }

    public void setCC0(int CC0) {
        this.CC0 = CC0;
    }


    @XmlTransient
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

}
