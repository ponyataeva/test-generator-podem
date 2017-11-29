package model.entities;

import model.entities.impl.Value;
import model.entities.impl.OperationImpl;

import java.util.*;

/**
 * Consists preconditions and output.
 */
public class Gate implements Comparable<Gate> {

    private SortedSet<Fact> inputs;
    private Fact output;
    private Operation operation = OperationImpl.AND;
    private int index;
    private Integer ruleId;

    public Gate(SortedSet<Fact> inputs, Fact output) {
        this.inputs = inputs;
        this.output = output;
    }

    public void addInputs(SortedSet<Fact> inputs) {
        this.inputs.addAll(inputs);
    }

    public Set<Fact> getInputs() {
        return inputs;
    }

    public Fact getOutput() {
        return output;
    }

    public boolean containsInput(Fact fact) {
        for (Fact ruleFact : inputs) {
            if (ruleFact.equals(fact)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsOutput(Fact fact) {
        return output.equals(fact);
    }

    public boolean hasDInput() {
        for (Fact input : inputs) {
            if (input.getValue().equals(Value.D) ||
                    input.getValue().equals(Value.NOT_D)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllInputsAssigned() {
        for (Fact fact : inputs) {
            if (fact.isUnassigned()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Execute gate operation under all inputs.
     */
    public void simulate() {
        if (output.isPrimaryOutput() && !isAllInputsAssigned()) {
            return;
        }

        List<Value> values = new ArrayList<>();
        for (Fact input : inputs) {
            values.add(input.getValue());
        }

        Value result = operation.execute(values);
        output.setValue(result);
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int calculateCC0() {
        return operation.calculateCC0(inputs.toArray(new Fact[inputs.size()]));
    }

    public int calculateCC1() {
        return operation.calculateCC1(inputs.toArray(new Fact[inputs.size()]));
    }

    public Fact getEasierPathCC0() {
        Fact result = null;
        for (Fact input : inputs) {
            if ((result == null || input.getCC0() < result.getCC0()) && isUnassignedPI(input)) {
                result = input;
            }
        }
        return result;
    }

    public Fact getEasierPathCC1() {
        Fact result = null;
        for (Fact input : inputs) {
            if (result == null || input.getCC1() < result.getCC1() && isUnassignedPI(input)) {
                result = input;
            }
        }
        return result;
    }

    public Fact getHardestPathCC0() {
        Fact result = null;
        for (Fact input : inputs) {
            if ((result == null || input.getCC0() > result.getCC0()) && isUnassignedPI(input)) {
                result = input;
            }
        }
        return result;
    }

    public Fact getHardestPathCC1() {
        Fact result = null;
        for (Fact input : inputs) {
            if ((result == null || input.getCC1() > result.getCC1()) && isUnassignedPI(input)) {
                result = input;
            }
        }
        return result;
    }

    private boolean isUnassignedPI(Fact fact) {
        return fact.isPrimaryInput() && (fact.isUnassigned() || fact.isAlternateAssignment());
    }

    public boolean hasNonControllingValue() {
        return operation.getNonControllingValue().equals(output.getValue());
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public int compareTo(Gate o) {
        return this.getIndex().compareTo(o.getIndex());
    }

    @Override
    public String toString() {
        return "\nGate{" +
                "inputs=" + inputs +
                ",output=" + output +
                '}';
    }
}
