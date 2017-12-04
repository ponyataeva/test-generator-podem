package model.entities.utils;

import model.entities.Fact;

import java.util.HashSet;
import java.util.Set;

/**
 * Utils class for Fact objects.
 */
public class FactUtils {

    private static Set<Fact> allFacts = new HashSet<>();

    /**
     * Multitone for the fact objects.
     * <p>
     * Find by the name object. If object already exists return it.
     * else create new object and set index to it.
     *
     * @param factName - name of the fact
     * @return the fact object with index.
     */
    public static Fact getFact(String factName, int index) {
        Fact resultFact = new Fact(factName);
        for (Fact fact : allFacts) {
            if (fact.equals(resultFact)) {
                return fact;
            }
        }
        resultFact.setIndex(index);
        allFacts.add(resultFact);

        return resultFact;
    }

    public static Fact getFact(String name) {
        return allFacts.stream()
                .filter(fact -> name.equals(fact.getName()))
                .findFirst().get();
    }

    public static Fact getFact(int index) {
        for (Fact fact : allFacts) {
            if (fact.getIndex().equals(index)) {
                return fact;
            }
        }
        return null;
    }
}
