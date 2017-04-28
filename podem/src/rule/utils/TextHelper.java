package rule.utils;

import entities.Rule;
import entities.State;
import entities.StateUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Add class description
 */
public class TextHelper {

    private static final List<String> PATTERNS = Arrays.asList(
            RulePattern.SINGLE_CONDITION,
            RulePattern.MULTIPLE_CONDITION);

    public static String getRuleNumber(String line) {
        String rulePart = findPart(line, RulePattern.RULE_NUMBER);
        if (rulePart != null) {
            return findPart(rulePart, "[0-9]+");
        }
        return null;
    }

    private static Set<State> getPreconditions(String line) {
        String preconditionsPart = getPreconditionPart(line);
        if (preconditionsPart == null) {
            return Collections.EMPTY_SET;
        }
        String[] preconditions = preconditionsPart.split("\\s*и\\s*");
        Set<State> result = new HashSet<>();
        for (String preconditionName : preconditions) {
            State precondition = StateUtils.getState(preconditionName.replaceAll("\"", ""));
            result.add(precondition);
        }
        return result;
    }

    private static String getAction(String line) {
        String action = findPart(line, RulePattern.DESTINATION);
        if (action != null) {
            return action.trim().replaceAll(RulePattern.SPACE + RulePattern.THEN + RulePattern.SPACE, "").replaceAll("\"", "");
        }
        return action;
    }

    private static String getPreconditionPart(String line) {
        for (String pattern : PATTERNS) {
            String preconditions = findPart(line, pattern);
            if (preconditions != null) {
                return preconditions.replaceAll(",$", "");
            }
        }
        return null;
    }

    public static List<Rule> parse(List<String> lines) {
        List<Rule> rules = new ArrayList<>();
        for (String line : lines) {
            Set<State> preconditions = getPreconditions(line);
            State action = StateUtils.getState(getAction(line));
            rules.add(new Rule(preconditions, action));
        }
        return rules;
    }

    private static String findPart(String line, String regexp) {
        Matcher matcher = Pattern.compile(regexp).matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
