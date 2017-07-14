import entities.State;
import org.apache.commons.lang.StringUtils;

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
 *
 * @return
 */
public class Validator {

    private static final String DUPLICATES_FOUND = "The list of facts contain next duplicate(s) for %s:\n%sIt(s) was removed.";

    public static void validateDuplicates(List<State> facts) {
        List<State> results = new ArrayList<>(facts);
        for (int i = 0; i < facts.size(); i++) {
            State fact = facts.get(i);
            String duplicatesList = "";
            for (int j = i + 1; j < facts.size(); j++) {
                State duplicate = facts.get(j);
                boolean isEqualName = fact.getName().equals(duplicate.getName());
                if (isEqualName) {
                    if (results.remove(duplicate)) {
                        duplicatesList += duplicate.toString() + "\n";
                    }
                }
            }
            if (StringUtils.isNotEmpty(duplicatesList)) {
                printFormattedMessage(DUPLICATES_FOUND, fact.toString(), duplicatesList);
            }
        }

        System.out.println("Result:" + Arrays.toString(results.toArray()));
    }

    public static void validateDuplicates(List<State> facts, State newFact) {
        for (State state : facts) {
            if (state.getName().equals(newFact)) {

            }
        }
    }

    private static void printFormattedMessage(String message, String... v) {
        System.out.println(String.format(message, v));
    }
}
