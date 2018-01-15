package model.entities.utils;

import model.entities.Fact;

import java.util.HashSet;
import java.util.Set;

/**
 * Utils class for Fact objects.
 */
public class FactUtils {

    private static Set<Fact> allFacts = new HashSet<>();

    public static Set<Fact> getAllFacts() {
        return allFacts;
    }

    /**
     * Multitone for the fact objects.
     * <p>
     * Find by the name object. If object already exists return it.
     * else create new object and set index to it.
     *
     * @param factName - name of the fact
     * @return the fact object with index.
     */
    public static Fact createFact(String factName, int index) {
//        Fact resultFact = new Fact(factName);
        return allFacts.stream()
                .filter(fact -> fact.getName().equals(factName)).findFirst()
                .orElseGet(() -> {
                    Fact fact = new Fact();
                    fact.setName(factName);
                    fact.setIndex(index);
                    allFacts.add(fact);
                    return fact;
                });
    }

    public static Fact getFact(String name) {
        return allFacts.stream()
                .filter(fact -> name.equals(fact.getName()))
                .findFirst().get();
    }

    public static Fact getFact(int index) {
        return allFacts.stream()
                .filter(fact -> fact.getIndex().equals(index))
                .findFirst().get();
    }
}
