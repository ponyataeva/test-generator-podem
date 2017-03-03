package format;

/**
 * Add class description
 */
public interface FilePattern {

    String SPACE = "\\s*";
    String RULE = "R[0-9]+:";
    String IF = "Если";
    String CONDITION = "\".*\"";
    String SINGLE_CONDITION = "(" + CONDITION + "){1}" + SPACE + "," ;
    String MULTIPLE_CONDITION = "(" + CONDITION + SPACE + "и" + SPACE + CONDITION + ")+" + SPACE + "и"+ SPACE + SINGLE_CONDITION;
    String THEN = "то";
    String DESTINATION = THEN + SPACE + "(" + CONDITION + "){1}";

    String PATTERN = RULE + SPACE + IF + SPACE + "(" + SINGLE_CONDITION + "|" + MULTIPLE_CONDITION + ")" + SPACE + DESTINATION;
//    String PATTERN = "R[0-9]+:\\s*Если\\s*((\".*\"){1}|(\".*\"\\sи\\s)+\".*\"\\s)\\s*,\\s*то\\s*\".*\"\\s*";
}
