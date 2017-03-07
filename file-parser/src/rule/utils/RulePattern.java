package rule.utils;

/**
 * Patterns constants.
 */
public interface RulePattern {

    String SPACE = "\\s*";
    String RULE_NUMBER = "R[0-9]+:";
    String IF = "Если";
    String CONDITION = "\".*\"";
    String SINGLE_CONDITION = "(" + CONDITION + "){1}" + SPACE + "," ;
    String MULTIPLE_CONDITION = "(" + CONDITION + SPACE + "и" + SPACE + CONDITION + ")+" + SPACE + "и"+ SPACE + SINGLE_CONDITION;
    String THEN = "то";
    String DESTINATION = THEN + SPACE + "(" + CONDITION + "){1}";

    String PATTERN = RULE_NUMBER + SPACE + IF + SPACE + "(" + SINGLE_CONDITION + "|" + MULTIPLE_CONDITION + ")" + SPACE + DESTINATION;
}
