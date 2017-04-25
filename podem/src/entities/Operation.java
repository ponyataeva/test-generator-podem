package entities;

/**
 * Operation over T6 alphabet.
 */
public interface Operation {

    Alphabet and(Alphabet alphabet);

    Alphabet or(Alphabet alphabet);

    Alphabet not();
}
