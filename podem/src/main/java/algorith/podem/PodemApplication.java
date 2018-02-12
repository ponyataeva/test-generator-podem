package algorith.podem;

import model.entities.*;
import model.entities.Rule;
import model.entities.utils.FactUtils;
import xml.parser.parse.XmlParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static model.entities.Operation.NAND;
import static model.entities.Value.*;

public class PodemApplication {

    private List<Test> generatedTests = new ArrayList<>();
    private Scheme scheme;
    private Fact fault;
    private Stack implication = new Stack();
    private List<Fact> propagationPath = new ArrayList<>();

    // TODO add static creation of class
    // TODO and objects can be compared by ==
    // TODO переопределить hashCode везде, где переопределен equals
    public static void main(String[] args) throws IOException {
        Set<Rule> rules = XmlParser.parseDefaultCfg();
        Scheme scheme = new Scheme(rules);

        FactUtils.getAllFacts().forEach(fact -> {
            fact.setFaultType(FaultType.sa0);
            try {
                new PodemApplication(scheme, fact).execute();
                System.out.println(scheme.getTest());
                scheme.print();
            } catch (Exception e) {
                System.out.println(e);
            }
            scheme.clear();
            fact.setFaultType(FaultType.NONE);
            System.out.println("====================================================================");
        });
    }

    public PodemApplication(Scheme scheme, Fact fault) {
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
        List<Rule> dFrontier = getDFrontier(fault);
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
            for (Rule rule : fact.isInputFor()) {
                fact = rule.getOutput();
                if (fact.isUnassigned()) {
                    rule.simulate();
                } else if (fact.equals(fault) && !fact.hasDisagreementValue()) {
                    Value tempValue = fact.getValue();
                    rule.simulate();
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
                    rule.simulate();
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
            for (Rule rule : fact.isOutputFor()) {
                if (rule.getOperation().equals(NAND)) {
                    tempValue = tempValue.not();
                }
                if (allInputsNeedSet(rule, fact)) {
                    if (fact.getValue().equals(ONE)) {
                        fact = rule.getHardestPathCC1();
                    } else {
                        fact = rule.getHardestPathCC0();
                    }
                } else {
                    if (fact.getValue().equals(ONE)) {
                        fact = rule.getEasierPathCC1();
                    } else {
                        fact = rule.getEasierPathCC0();
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

    private boolean allInputsNeedSet(Rule rule, Fact output) {
        return rule.getOperation() == NAND && output.getValue().equals(ZERO);
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

        for (Rule target : getDFrontier(fault)) {
            Fact output = target.getOutput();
            // find the shortest unassigned path
            if (!propagationPath.contains(output) || output.isAssigned()) {
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
    private boolean xPathCheck(List<Rule> dFrontier) {
        for (Rule rule : dFrontier) {
            Fact out = rule.getOutput();
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
    private List<Rule> getDFrontier(Fact fault) {
        List<Rule> dFrontier = new ArrayList<>();
        for (Rule rule : fault.isInputFor()) {
            if (rule.getOutput().isUnassigned() && rule.hasDInput()) {
                dFrontier.add(rule);
            }

            dFrontier.addAll(getDFrontier(rule.getOutput()));
        }
        return dFrontier;
    }

    private boolean faultIsPropagated() {
        for (Fact fact : propagationPath) {
            if (!(fact.getValue().equals(D) || fact.getValue().equals(NOT_D))) {
                return false;
            }
        }
        return true;
    }

    private void findPropagationPath() {
        propagationPath.add(fault);
        propagationPath.addAll(searchPath(fault));
        System.out.println("Fault Propagation path is " + propagationPath);
    }

    private List<Fact> searchPath(Fact fact) {
        List<Fact> temp = new ArrayList<>();
        List<Fact> minPath = new ArrayList<>();
        for (Rule rule : fact.isInputFor()) {
            Fact out = rule.getOutput();
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
