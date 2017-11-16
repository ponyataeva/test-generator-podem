package rules.validation;

import model.entities.Root;
import model.entities.Rule;
import model.entities.State;
import rules.parser.utils.XMLValidationException;

import java.util.List;

import static java.lang.String.format;

/**
 * TODO выявлять противоречия среди правил
 * + правила долны нумероваться
 * + в одном правиле факт и его инверсия
 * + и в условии и в следствии не может быть один факт
 * + одинаковые условия в правиле, но разные результат
 * + отрицание только в первой части
 * отрицание часть правила
 * <p>
 * v2.
 * + Проверять что нумерация фактов и правил не дублируется.
 */
public class Validator {

    private static final String DUPLICATES_FOUND = "The list of facts contain next duplicate(s):";
    private static final String DUPLICATE_ARGS = "\nFor %s: %s";
    private static final String INVERSIONS_FOUND = "For fact '%s' in the rule '%s' contains him inversions";
    private static final String DUPLICATES_IN_RULE_FOUND = "The rule '%s' contains same fact in condition and action parts";
    private static final String NEGATION_IN_OUT = "The rule '%s' contains negation in action part";
    private static final String SAME_CONDITIONS_OTHER_ACTION = "The rule '%s' has the same condition(s) part as '%s' but other action part.";

    private Root root;

    public Validator(Root root) {
        this.root = root;
    }

    public void doValidation() {
        validateFacts(this.root.getStates());
        validateRules(this.root.getRules());
    }

    /**
     * Find duplicate Facts' definition list.
     * Will print all duplicates except for the first.
     *
     * @param facts dirty list of facts.
     */
    private void validateFacts(List<State> facts) {
        StringBuilder args = new StringBuilder("");
        for (int i = 0; i < facts.size(); i++) {
            State fact = facts.get(i);
            StringBuilder duplicatesList = new StringBuilder("");

            for (int j = i + 1; j < facts.size(); j++) {
                State duplicate = facts.get(j);
                boolean isEqualName = fact.getName().equals(duplicate.getName());
                if (isEqualName) {
                    duplicatesList.append(duplicate.toString());
                }
            }
            if (duplicatesList.length() != 0) {
                args.append(format(DUPLICATE_ARGS, fact.toString(), duplicatesList));
            }
        }
        if (args.length() != 0) {
            throw new XMLValidationException(DUPLICATES_FOUND + args);
        }
    }

    /**
     * Check at less one rule contains fact and him inversion itself.
     * Printed all rules which contain inversion.
     *
     * @param rules list of rules for check.
     */
    private void validateRules(List<Rule> rules) {
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            validateNegation(rule);
            validateDuplicateInRules(rule);
            validateSameConditionsWithOtherAction(rule, rules);
        }
    }

    private static void validateSameConditionsWithOtherAction(Rule source, List<Rule> target) {
        int i = target.indexOf(source);
        for (int j = i + 1; j < target.size(); j++) {
            Rule temp = target.get(j);
            if (source.getInputs().equals(temp.getInputs()) && !(source.getOutput().equals(temp.getOutput()))) {
                throw new XMLValidationException(format(SAME_CONDITIONS_OTHER_ACTION, temp.getIndex(), source.getIndex()));
            }
        }
    }

    private static void validateNegation(Rule rule) {
        if (rule.getOutput().isNegation()) {
            throw new XMLValidationException(format(NEGATION_IN_OUT, rule.toString()));
        }
    }

    private static void validateDuplicateInRules(Rule rule) {
        State out = rule.getOutput();
        for (State fact : rule.getInputs()) {
            if (out.equals(fact)) {
                if (rule.isNegation()) {
                    throw new XMLValidationException(format(INVERSIONS_FOUND, fact.toString(), rule.toString()));
                }
                throw new XMLValidationException(format(DUPLICATES_IN_RULE_FOUND, rule.toString()));
            }
        }
    }
}
