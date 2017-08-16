package validation;

import entities.Root;
import entities.Rule;
import entities.State;
import utils.XMLValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO выявлять противоречия среди правил
 * правила долны нумероваться
 * в одном правиле факт и его инверсия
 * и в условии и в следствии не может быть один факт
 * одинаковые условия в правиле, о разные результат
 * отрицание только в первой части
 * отрицание часть правила
 */
public class Validator {

    private static final String DUPLICATES_FOUND = "The list of facts contain next duplicate(s):";
    private static final String DUPLICATE_ARGS = "\nFor %s: %s";
    private static final String INVERSIONS_FOUND = "\nFor fact '%s' in the rule '%s' contains him inversions";
    private static final String DUPLICATES_IN_RULE_FOUND = "\nThe rule '%s' contains same fact in condition and action parts";

    private Root root;

    public Validator(Root root) {
        this.root = root;
    }

    public void doValidation() {
        checkDuplicates(this.root.getStates());
        validateInversionsExists(this.root.getRules());
    }

    /**
     * Find duplicate Facts.
     * Will print all duplicates except for the first.
     *
     * @param facts dirty list of facts.
     * @return clear list of facts.
     */
    private static List<State> checkDuplicates(List<State> facts) {
        List<State> results = new ArrayList<>(facts);
        StringBuilder args = new StringBuilder("");
        for (int i = 0; i < facts.size(); i++) {
            State fact = facts.get(i);
            StringBuilder duplicatesList = new StringBuilder("");
            for (int j = i + 1; j < facts.size(); j++) {
                State duplicate = facts.get(j);
                boolean isEqualName = fact.getName().equals(duplicate.getName());
                if (isEqualName && results.remove(duplicate)) {
                    duplicatesList.append(duplicate.toString());
                }
            }
            if (duplicatesList.length() != 0) {
                args.append(getFormattedMessage(DUPLICATE_ARGS, fact.toString(), duplicatesList));
            }
        }
        if (args.length() != 0) {
            throw new XMLValidationException(DUPLICATES_FOUND + args);
        }

        System.out.println("Result:" + Arrays.toString(results.toArray()));
        return results;
    }

    /**
     * Check at less one rule contains fact and him inversion itself.
     * Printed all rules which contain inversion.
     *
     * @param rules list of rules for check.
     * @return true if inversion was found.
     */
    private static void validateInversionsExists(List<Rule> rules) {
        StringBuilder errMsg = new StringBuilder("");
        for (Rule rule : rules) {
            State out = rule.getOutput();
            for (State fact : rule.getInputs()) {
                if (out.equals(fact)) {
                    errMsg.append(getFormattedMessage(DUPLICATES_IN_RULE_FOUND, rule.toString()));
                    if (rule.isNegation()) {
                        errMsg.append(getFormattedMessage(INVERSIONS_FOUND, fact.toString(), rule.toString()));
                    }
                }
            }
        }
        if (errMsg.length() != 0) {
            throw new XMLValidationException(errMsg.toString());
        }
    }

    private static String getFormattedMessage(String message, Object... v) {
        return String.format(message, v);
    }
}
