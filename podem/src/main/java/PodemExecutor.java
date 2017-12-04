import model.entities.Gate;
import model.entities.Scheme;
import model.entities.Fact;
import model.entities.Test;
import model.entities.impl.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static model.entities.impl.OperationImpl.NAND;
import static model.entities.impl.Value.*;

/**
 * Add class description
 */
public class PodemExecutor {

    private List<Test> generatedTests = new ArrayList<>();
    private Scheme scheme;
    private Fact fault;
    private Stack implication = new Stack();
    private List<Fact> propogationPath = new ArrayList<>();

    public PodemExecutor(Scheme scheme, Fact fault) {
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


        Fact fact = objective(); // obtain objective
        fact = backtrace(fact); // there is state is a PI
        implication.push(fact);
        fact.setAlternateAssignmentTried(false);
        imply(fact);

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

            fact.setValue(fact.getValue().not()); // reverse decision backtrack
            imply(fact);
        } else {
            while (!isExhausted()) {
                fact = (Fact) implication.pop();
                if (!fact.isAlternateAssignment()) { // TODO rewrite
                    implication.push(fact);
                    fact.setValue(fact.getValue().not());
                    fact.setAlternateAssignmentTried(true);
                    System.out.println("Try to alternate assignment for " + fact);

                    imply(fact);

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
        if (scheme.getPIs().size() == implication.size()) {
            Fact fact = (Fact) implication.pop();
            if (fact.isAlternateAssignment()) {
                return true;
            }
            implication.push(fact);
        } else if (implication.size() == 0) {
            return true;
        }
        return false;
    }


    /**
     * When the input to a logic gate are significantly labeled,
     * the output can be uniquely determined.
     *
     * @param fact
     */
    private void imply(Fact fact) {
        while (!fact.isPrimaryOutput()) {
            for (Gate gate : fact.isInputFor()) {
                fact = gate.getOutput();
                if (fact.isUnassigned()) {
                    gate.simulate();
                } else if (fact.equals(fault) && !fact.hasDisagreementValue()) {
                    Value tempValue = fact.getValue();
                    gate.simulate();
                    if (fact.getValue().equals(tempValue)) {
                        if (fact.getValue().equals(ONE)) {
                            fact.setValue(D);
                        } else if (fact.getValue().equals(ZERO)) {
                            fact.setValue(NOT_D);
                        }
                    } else {
                        fact.setValue(X);
                    }
                } else if (fact.isAlternateAssignment()) {
                    gate.simulate();
                }
            }
        }
    }

    /**
     * Map a desired objective into a PI assignment.
     *
     * @param fact is objective.
     * @return PI wih assigned value
     */

    private Fact backtrace(Fact fact) {
        Value tempValue = fact.getValue();
        while (!fact.isPrimaryInput()) {
            for (Gate gate : fact.isOutputFor()) {
                if (gate.getOperation().equals(NAND)) {
                    tempValue = tempValue.not();
                }
                if (allInputsNeedSet(gate, fact)) {
                    if (fact.getValue().equals(ONE)) {
                        fact = gate.getHardestPathCC1();
                    } else {
                        fact = gate.getHardestPathCC0();
                    }
                } else {
                    if (fact.getValue().equals(ONE)) {
                        fact = gate.getEasierPathCC1();
                    } else {
                        fact = gate.getEasierPathCC0();
                    }
                    Fact b = backtrace(fact);
                    if (b != null) {
                        fact = b;
                    }
                }
            }
        }
        fact.setValue(tempValue);
        return fact;
    }

    private boolean allInputsNeedSet(Gate g, Fact output) {
        return g.getOperation() == NAND && output.getValue().equals(ZERO);
    }

    /**
     * Obtain objective
     *
     * @return a gate from D-frontier
     */
    private Fact objective() {
        Value objValue = fault.getFaultType().getValue().not();
        if (fault.isUnassigned()) { // activate fault
            fault.setValue(objValue);
            return fault;
        }

        for (Gate target : getDFrontier(fault)) {
            // TODO how to any states settings to D ?
            Fact output = target.getOutput();
            // find the shortest unassigned path
            if (!propogationPath.contains(output) || output.isAssigned()) {
                continue;
            }
            if (target.hasNonControllingValue()) {
                objValue = target.getOperation().getNonControllingValue();
            }
            for (Fact input : target.getInputs()) {
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
            Fact out = gate.getOutput();
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
    private List<Gate> getDFrontier(Fact fault) {
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
        for (Fact fact : propogationPath) {
            if (!(fact.getValue().equals(D) || fact.getValue().equals(NOT_D))) {
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

    private List<Fact> searchPath(Fact fact) {
        List<Fact> temp = new ArrayList<>();
        List<Fact> minPath = new ArrayList<>();
        for (Gate gate : fact.isInputFor()) {
            Fact out = gate.getOutput();
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
