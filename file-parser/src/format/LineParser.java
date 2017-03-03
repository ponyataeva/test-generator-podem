package format;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static format.RulePattern.*;

/**
 * Add class description
 */
public class LineParser {

    public static String getRuleNumber(String line) {
        String rulePart = findPart(line, RULE_NUMBER);
        if (rulePart != null) {
            return findPart(rulePart, "[0-9]+");
        }
        return null;
    }

    public static List<String> getConditionsList(String line) {
        String conditions = getCondition(line);
        if (conditions == null) {
            return null;
        }
        String[] conditionsMas = conditions.split("\\s*и\\s*");
        List<String> result = new ArrayList<>();
        for (String condition : conditionsMas) {
            result.add(condition.replaceAll("\"", ""));
        }
        return result;
    }

    public static String getDestination(String line) {
        String destination = findPart(line, DESTINATION);
        if (destination != null) {
            return destination.trim().replaceAll(SPACE + THEN + SPACE, "").replaceAll("\"", "");
        }
        return destination;
    }

    public static String getCondition(String line) {
        String conditions = findPart(line, MULTIPLE_CONDITION);
        if (conditions == null) {
            conditions = findPart(line, SINGLE_CONDITION);
            if (conditions != null) {
                return conditions.replaceAll(",$", "");
            }
            return conditions;
        }
        return conditions.replaceAll(",$", "");
    }

    public static Map<Set<Condition>, Condition> parse(List<String> lines) {
        Map<Set<Condition>, Condition> rules = new HashMap<>();
        Set<Condition> allConditions = new HashSet<>();
        for (String line : lines) {
            Set<Condition> conditions = new HashSet<>();
            for (String condition : getConditionsList(line)) {
                Condition condition1 = new Condition(condition);
                if (allConditions.contains(condition1)) {
                    Iterator iterator = allConditions.iterator();
                    while (iterator.hasNext()) {
                        Condition curr = (Condition) iterator.next();
                        if (curr.equals(condition1)) {
                            conditions.add(curr);
                            break;
                        }
                    }
                } else {
                    allConditions.add(condition1);
                    conditions.add(condition1);
                }
            }
            rules.put(conditions, new Condition(getDestination(line)));
        } return rules;
    }

    private static String findPart(String line, String regexp) {
        Matcher matcher = Pattern.compile(regexp).matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }
}
