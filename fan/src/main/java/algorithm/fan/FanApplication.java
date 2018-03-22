package algorithm.fan;

import algorithm.podem.PodemApplication;
import model.entities.*;
import model.entities.utils.FactUtils;
import xml.parser.parse.XmlParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import static model.entities.Operation.NAND;
import static model.entities.Value.*;

public class FanApplication extends PodemApplication {

    public static void main(String[] args) {
        Set<Rule> rules = XmlParser.parseDefaultCfg();
        Scheme scheme = new Scheme(rules);
        FanApplication app = new FanApplication();

        FactUtils.getFact("K").setFaultType(FaultType.sa0);
            try {
                app.execute(scheme, FactUtils.getFact("K"));
                System.out.println(scheme.getTest());
                scheme.print();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("====================================================================");
    }

    private List<Test> generatedTests = new ArrayList<>();
    private Scheme scheme;
    private Fact fault;
    private Stack implication = new Stack();
    private List<Fact> propagationPath = new ArrayList<>();


    public boolean execute(Scheme scheme, Fact fault) {
        // select path for fault propagation from fault to neares PO
        // select objective
        // backtrace from objective (set only PI) and put this PI to implication stack
        // forward implication:
        // values after fault should be D or not D

        this.scheme = scheme;
        this.fault = fault;
        findPropagationPath();

        Fact fact = objective(); // obtain objective
        fact = backtrace(fact); // there is state is a PI
        implication.push(fact);
        fact.setAlternateAssignmentTried(false);
        imply(fact);

        if (faultIsPropagated()) {
            return true;
        }
        List<Rule> dFrontier = getDFrontier(fault);
//        if (xPathCheck(dFrontier)) { // is test possible

            if (execute(scheme, fault)) {
                System.out.println("Success! Test was generated");
                generatedTests.add(scheme.getTest());
                return true;
            }

            fact.setValue(fact.getValue().not()); // reverse decision backtrack
            imply(fact);
//        } else {
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
                }
            }
            throw new IllegalStateException("Fault unstable");
//        }
//        return false;
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
