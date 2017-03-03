package format;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Add class description
 */
public class GraphUtils {

    public static int[][] toMatrix(Map<Set<Condition>, Condition> conditions) {
        Set<Condition> allConditions = new HashSet<>();
        int i = 0;
        for (Map.Entry<Set<Condition>, Condition> conditionPair : conditions.entrySet()) {
            for (Condition condition : conditionPair.getKey()) {
                allConditions.add(condition);
            }
            allConditions.add(conditionPair.getValue());
        }

        for (Condition condition : allConditions) {
            condition.setIndex(i);
            i++;
        }
        int[][] result = new int[allConditions.size()][allConditions.size()];

        for (Map.Entry<Set<Condition>, Condition> conditionPair : conditions.entrySet()) {
            int destinationIndex = conditionPair.getValue().getIndex();
            for (Condition condition : conditionPair.getKey()) {
                result[condition.getIndex()][destinationIndex] = 1;
            }
        }
        return result;
    }
}
