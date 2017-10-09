package rule.utils;

import model.entities.Gate;
import model.entities.State;
import model.entities.utils.GateUtils;
import model.entities.utils.StateUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static model.entities.impl.OperationImpl.NAND;

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

    private static SortedSet<State> getPreconditions(String line) {
        String preconditionsPart = getPreconditionPart(line);
        if (preconditionsPart == null) {
            return new TreeSet<>();
        }
        String[] preconditions = preconditionsPart.split("\\s*и\\s*");
        SortedSet<State> result = new TreeSet<>();
        for (String preconditionName : preconditions) {
            State precondition = StateUtils.getState(preconditionName.replaceAll("\"", ""));
            result.add(precondition);
        }
        return result;
    }

    private static String getAction(String line) {
        String action = findPart(line, RulePattern.DESTINATION);
        if (action != null) {
            return action.trim().replaceAll(RulePattern.SPACE + RulePattern.THEN + RulePattern.SPACE, "").replaceAll("\"", "").replaceAll("не\\s", "");
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

    public static Set<Gate> parse(List<String> lines) {
        Set<Gate> gates = new HashSet<>();
        for (String line : lines) {
            SortedSet<State> preconditions = getPreconditions(line);
            State action = StateUtils.getState(getAction(line));
            Gate g = GateUtils.getGate(preconditions, action);
            gates.add(g);
            if (line.contains("то не")) {
                g.setOperation(NAND);
            }
        }
        return gates;
    }

    private static String findPart(String line, String regexp) {
        Matcher matcher = Pattern.compile(regexp).matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
