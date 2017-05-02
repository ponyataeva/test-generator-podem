package entities;

/**
 * Operation over T6 alphabet.
 */
interface Operation {

    Alphabet and(Alphabet alphabet);

    Alphabet or(Alphabet alphabet);

    Alphabet not();
}
