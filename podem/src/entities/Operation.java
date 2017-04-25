package entities;

/**
 * Add class description
 */
public interface Operation {

    Alphabet and(Alphabet alphabet);

    Alphabet or(Alphabet alphabet);

    Alphabet not();
}
