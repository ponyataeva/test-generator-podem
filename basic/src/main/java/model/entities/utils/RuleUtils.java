package model.entities.utils;

import model.entities.Fact;
import model.entities.Rule;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Add class description
 */
public class RuleUtils {

    private static Set<Rule> allRules = new HashSet<>();
    private static AtomicInteger sequence = new AtomicInteger();

    public static Rule getRule(SortedSet<Fact> inputs, Fact output) {
        return allRules.stream()
                .filter(rule -> rule.getOutput().equals(output))
                .peek(rule -> rule.addInputs(inputs))
                .peek(rule -> inputs.forEach(input -> input.addIsInputFor(rule)))
                .findFirst().orElse(createNewRule(inputs, output));
    }

    private static Rule createNewRule(SortedSet<Fact> inputs, Fact output) {
        Rule resultRule = new Rule(inputs, output);
        resultRule.setIndex(sequence.incrementAndGet());
        allRules.add(resultRule);

        output.addIsOutputFor(resultRule);
        inputs.forEach(input -> input.addIsInputFor(resultRule));

        return resultRule;
    }

    public static Set<Rule> getAllRules() {
        return allRules;
    }
}
