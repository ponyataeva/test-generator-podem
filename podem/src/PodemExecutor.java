import antlr.collections.Stack;
import antlr.collections.impl.LList;
import entities.*;
import entities.impl.Value;

import java.util.ArrayList;
import java.util.List;

import static entities.impl.OperationImpl.NAND;
import static entities.impl.Value.*;

/**
 * Add class description
 */
public class PodemExecutor {

    private List<Test> generatedTests = new ArrayList<>();
    private Scheme scheme;
    private State fault;
    private Stack implication = new LList();
    private List<State> propogationPath = new ArrayList<>();

    public PodemExecutor(Scheme scheme, State fault) {
        this.scheme = scheme;
        this.fault = fault;
        findPropagationPath();
    }

    public boolean execute() {
        // select path for fault propagation from fault to neares PO
        // select objective
        // backtrace from objective (set only PI) and put this PI to implication stack
        // forward implication:
        // values after fault should be D or not D


        while (!faultIsPropagated()) {
            List<Gate> dFrontier = getDFrontier(fault);
//            if (xPathCheck(dFrontier)) { // is test possible
            State state = objective(); // obtain objective
            state = backtrace(state); // rhere is state is a PI
            implication.push(state);
            imply(state);

            if (execute()) {
                System.out.println("Success " + scheme.getPIs());
                generatedTests.add(scheme.getTest());
                return true;
            }

            state.setValue(state.getValue().not()); // reverse decision TODO backtrack
            imply(state);
//            } else {
            while (!isExhausted()) {
                state = (State) implication.pop();
                if (!state.isAlternateAssignmentTried()) {
                    implication.push(state);
                    state.setValue(state.getValue().not());
                    System.out.println("Try to alternate assignment for " + state);

                    imply(state);

                    if (execute()) {
                        System.out.println("Success " + scheme.getPIs());
                        generatedTests.add(scheme.getTest());
                        return true;
                    }
                }
            }
            throw new IllegalStateException("Fault unstable");
//            }
        }
        return true;
    }

    /**
     * gets a list of the inputs that will sensitize the fault
     *
     * @return
     */

    private boolean isExhausted() {
        if (scheme.getPIs().size() == implication.height()) {
            State state = (State) implication.pop();
            if (state.isAlternateAssignmentTried()) {
                return true;
            }
            implication.push(state);
        }
        return false;
    }


    /**
     * When the input to a logic gate are significantly labeled,
     * the output can be uniquely determined.
     *
     * @param state
     */
    private void imply(State state) {
        while (!state.isPrimaryOutput()) {
            for (Gate gate : state.isInputFor()) {
                state = gate.getOutput();
                if (state.isUnassigned()) {
                    gate.simulate();
                } else if (state.equals(fault)) {
                    if (state.getValue().equals(ONE)) {
                        state.setValue(D);
                    } else if (state.getValue().equals(ZERO)) {
                        state.setValue(NOT_D);
                    }
                }
            }
        }
    }

    /**
     * Map a desired objective into a PI assignment.
     *
     * @param state is objective.
     * @return PI wih assigned value
     */
    private State backtrace(State state) {
        Value tempValue = state.getValue();
        while (!state.isPrimaryInput()) {
            for (Gate gate : state.isOutputFor()) {
                if (gate.getOperation().equals(NAND)) {
                    tempValue = tempValue.not();
                }
                if (allInputsNeedSet(gate, state)) {
                    if (state.getValue().equals(ONE)) {
                        state = gate.getHardestPathCC1();
                    } else {
                        state = gate.getHardestPathCC0();
                    }
                } else {
                    if (state.getValue().equals(ONE)) {
                        state = gate.getEasierPathCC1();
                    } else {
                        state = gate.getEasierPathCC0();
                    }
                    State b = backtrace(state);
                    if (b != null) {
                        state = b;
                    }
                }
            }
        }
        state.setValue(tempValue);
        return state;
    }

    private boolean allInputsNeedSet(Gate g, State output) {
        return g.getOperation() == NAND && output.getValue().equals(ZERO);
    }

    /**
     * Obtain objective
     *
     * @return a gate from D-frontier
     */
    private State objective() {
        Value objValue = fault.getFaultType().getValue().not();
//        State minCC0State = null;
//        for (Gate faultGate : fault.isOutputFor()) {
//            for (State faultInput : faultGate.getInputs()) {
//                if (faultInput.isUnassigned() && (minCC0State == null || faultInput.getCC0() < minCC0State.getCC0())) {
//                    minCC0State = faultInput;
//                }
//            }
//        }
//        if (minCC0State != null) {
//            minCC0State.setValue(fault.getFaultType().getValue());
//            return minCC0State;
//        }
        if (fault.isUnassigned()) { // activate fault
            fault.setValue(objValue);
            return fault;
        }

//        // propagate fault effect
//        // TODO It should execute only one time
//        if (fault.getValue().equals(ONE)) {
//            fault.setValue(D);
//        } else {
//            fault.setValue(NOT_D);
//        }

        for (Gate target : getDFrontier(fault)) {
            // TODO how to any states settings to D ?
            State output = target.getOutput();
            // find the shortest unassigned path
            if (!propogationPath.contains(output) || output.isAssigned()) {
                continue;
            }
            if (target.hasNonControllingValue()) {
                objValue = target.getOperation().getNonControllingValue();
            }
            for (State input : target.getInputs()) {
                if (input.isUnassigned()) {
                    input.setValue(objValue);
                    return input;
                }
            }
//            if (target.getOperation().equals(NAND)) {
//                targetValue = output.getValue().not();
//            } else {
//                targetValue = output.getValue();
//            }

        }
        return null;

//        if (fault.isUnassigned()) {
//            fault.setValue(fault.getFaultType().getValue());
//            return fault;
//        }
//
//        for (State state : propogationPath) {
//            if (state.isUnassigned()) {
//                state.setValue(state.isOutputFor().iterator().next().getOperation().getNonControllingValue()); // TODO rewrite
//                System.out.println("As objective was selected " + state);
//                return state;
//            }
//        }
//        return null;
//        for (Gate gate : getDFrontier(scheme)) {
//            for (State input : gate.getInputs()) {
//                if (input.isUnassigned()) {
//                    Value ctrlValue;
//                    if (fault.hasControllingValue(gate.getOperation())) {
//                        ctrlValue = gate.getOperation().getControllingValue();
//                    } else {
//                        ctrlValue = gate.getOperation().getControllingValue().not();
//                    }
//                    input.setValue(ctrlValue);
//                    return input;
//                }
//            }
//        }
//        return null;
    }

    /**
     * Introduced to check whether the D-frontier still existed
     *
     * @param dFrontier
     * @return
     */
    private boolean xPathCheck(List<Gate> dFrontier) {
        for (Gate gate : dFrontier) {
            State out = gate.getOutput();
            if (out.isUnassigned()) {
                return out.isPrimaryOutput() || xPathCheck(new ArrayList<>(out.isInputFor()));
            }
        }
        return false;
    }

    /**
     * The set of gates closest to the primary outputs
     * that have one or more D values on their inputs
     * and an X value on their output
     *
     * @return
     */
    private List<Gate> getDFrontier(State fault) {
        List<Gate> dFrontier = new ArrayList<>();
        for (Gate gate : fault.isInputFor()) {
            if (gate.getOutput().isUnassigned() && gate.hasDInput()) {
                dFrontier.add(gate);
            }

            dFrontier.addAll(getDFrontier(gate.getOutput()));
        }
        return dFrontier;
    }

    private boolean faultIsPropagated() {
        for (State state : propogationPath) {
            if (!state.getValue().equals(D) && !state.getValue().equals(NOT_D) && state.isUnassigned()) {
                return false;
            }
        }
        return true;
    }

    private void findPropagationPath() {
        propogationPath.add(fault);
        propogationPath.addAll(searchPath(fault));
        System.out.println("Fault Propagation path is " + propogationPath);
    }

    private List<State> searchPath(State state) {
        List<State> temp = new ArrayList<>();
        List<State> minPath = new ArrayList<>();
        for (Gate gate : state.isInputFor()) {
            State out = gate.getOutput();
            temp.add(out);
            if (out.isPrimaryOutput()) {
                return temp;
            } else {
                temp.addAll(searchPath(out));
                if (temp.size() < minPath.size() || minPath.isEmpty()) {
                    minPath.clear();
                    minPath.addAll(temp);
                }
            }
        }
        return minPath;
    }


    private void pathSensitization() {
//        - Fault Activation: Force tested node to opposite of fault value.
//        - Fault Propagation: Also called fault sensitization. Propagate the effect to one or
//        more POs.
//        - Line justification: Justify internal signal assignments made to activate and sensitize
//        faults.
        fault.setValue(fault.getFaultType().getValue().not());
        for (Gate g : fault.isOutputFor()) {
            for (State faultInput : g.getInputs()) {

            }
        }
    }

    public List<Test> getGeneratedTests() {
        return generatedTests;
    }
}
