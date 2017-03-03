package line.utils;

import entities.Rule;
import entities.State;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static line.utils.RulePattern.*;

/**
 * Add class description
 */
public class LineHelper {

    private static final List<String> PATTERNS = Arrays.asList(
            SINGLE_CONDITION,
            MULTIPLE_CONDITION);
    private static Set<State> allStates = new HashSet<>();
    private static int currentStateIndex = 0;

    public static String getRuleNumber(String line) {
        String rulePart = findPart(line, RULE_NUMBER);
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
            State precondition = getState(preconditionName.replaceAll("\"", ""));
            result.add(precondition);
        }
        return result;
    }

    private static String getAction(String line) {
        String action = findPart(line, DESTINATION);
        if (action != null) {
            return action.trim().replaceAll(SPACE + THEN + SPACE, "").replaceAll("\"", "");
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
            State action = getState(getAction(line));
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

    private static State getState(String stateName) {
        State resultState = new State(stateName);
        for (State state : allStates) {
            if (state.equals(resultState)) {
                return state;
            }
        }
        resultState.setIndex(currentStateIndex);
        currentStateIndex++;

        allStates.add(resultState);
        return resultState;
    }
}
