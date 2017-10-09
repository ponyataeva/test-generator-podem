import antlr.collections.Stack;
import antlr.collections.impl.LList;
import model.entities.Gate;
import model.entities.Scheme;
import model.entities.State;
import model.entities.Test;
import model.entities.impl.Value;

import java.util.ArrayList;
import java.util.List;

import static model.entities.impl.OperationImpl.NAND;
import static model.entities.impl.Value.*;

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

    /** TODO выявлять противоречия среди правил
     * правила долны нумероваться
     * в одном правиле факт и его инверсия
     * и в условии и в следствии не может быть один факт
     * одинаковые условия в правиле, о разные результат
     * отрицание только в первой части
     * отрицание часть правила
     *
     * @return
     */

    public boolean execute() {
        State state = objective(); // obtain objective
        state = backtrace(state); // there is state is a PI
        implication.push(state);
        state.setAlternateAssignmentTried(false);
        imply(state);

        if (faultIsPropagated()) {
            return true;
        }
        List<Gate> dFrontier = getDFrontier(fault);
        if (xPathCheck(dFrontier)) { // is test possible

            if (execute()) {
                System.out.println("Success! Test was generated");
                generatedTests.add(scheme.getTest());
                return true;
            }

            state.setValue(state.getValue().not()); // reverse decision backtrack
            imply(state);
        } else {
            while (!isExhausted()) {
                state = (State) implication.pop();
                if (!state.isAlternateAssignment()) { // TODO rewrite
                    implication.push(state);
                    state.setValue(state.getValue().not());
                    state.setAlternateAssignmentTried(true);
                    System.out.println("Try to alternate assignment for " + state);

                    imply(state);

                    if (faultIsPropagated()) {
                        return true;
                    }
                    if (xPathCheck(getDFrontier(fault))) { // is test possible
                        if (execute()) {
                            System.out.println("Success " + scheme.getPIs());
                            generatedTests.add(scheme.getTest());
                            return true;
                        }
                    }
                }
            }
            throw new IllegalStateException("Fault unstable");
        }
        return false;
    }

    /**
     * gets a list of the inputs that will sensitize the fault
     *
     * @return
     */

    private boolean isExhausted() {
        if (scheme.getPIs().size() == implication.height()) {
            State state = (State) implication.pop();
            if (state.isAlternateAssignment()) {
                return true;
            }
            implication.push(state);
        } else if (implication.height() == 0) {
            return true;
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
                } else if (state.equals(fault) && !state.hasDisagreementValue()) {
                    Value tempValue = state.getValue();
                    gate.simulate();
                    if (state.getValue().equals(tempValue)) {
                        if (state.getValue().equals(ONE)) {
                            state.setValue(D);
                        } else if (state.getValue().equals(ZERO)) {
                            state.setValue(NOT_D);
                        }
                    } else {
                        state.setValue(X);
                    }
                } else if (state.isAlternateAssignment()) {
                    gate.simulate();
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
        if (fault.isUnassigned()) { // activate fault
            fault.setValue(objValue);
            return fault;
        }

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
        }
        return null;
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
            if (!(state.getValue().equals(D) || state.getValue().equals(NOT_D))) {
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
}
