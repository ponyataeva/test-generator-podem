package entities;

import entities.impl.Value;

import java.util.Iterator;
import java.util.Set;

import static entities.impl.OperationImpl.AND;

/**
 * Consists preconditions and output.
 */
public class Gate {

    private Set<State> inputs;
    private State output;
    private Operation operation = AND;
    private int index;

    public Gate(Set<State> inputs, State output) {
        this.inputs = inputs;
        this.output = output;
    }

    public void addInputs(Set<State> inputs) {
        this.inputs.addAll(inputs);
    }

    public Set<State> getInputs() {
        return inputs;
    }

    public State getOutput() {
        return output;
    }

    public boolean containsInput(State state) {
        for (State ruleState : inputs) {
            if (ruleState.equals(state)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsOutput(State state) {
        return output.equals(state);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Gate{" +
                "inputs=" + inputs +
                ", output=" + output +
                ", operation=" + operation +
                ", index=" + index +
                '}';
    }

    public boolean hasDInput() {
        for (State input : inputs) {
            if (input.getValue().equals(Value.D) ||
                    input.getValue().equals(Value.NOT_D)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllInputsAssigned() {
        for (State state : inputs) {
            if (state.isUnassigned()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Execute date operation under all inputs.
     */
    public void simulate() {
        Iterator<State> iterator = inputs.iterator();
        Value result;
        if (iterator.hasNext()) {
            result = iterator.next().getValue();
        } else {
            return;
        }

        while (iterator.hasNext()) {
            result = operation.execute(result, iterator.next().getValue());
        }
        output.setValue(result);
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int calculateCC0() {
        return operation.calculateCC0(inputs.toArray(new State[inputs.size()]));
    }

    public int calculateCC1() {
        return operation.calculateCC1(inputs.toArray(new State[inputs.size()]));
    }
}
