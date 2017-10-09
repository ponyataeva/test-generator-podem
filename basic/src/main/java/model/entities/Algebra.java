package model.entities;

import model.entities.impl.Value;

/**
 * Algebra over T6 alphabet.
 */
public interface Algebra {

    Value and(Value value);

    Value or(Value value);

    Value not();

    State getEasiestInput(Gate gate);

    State getHardestInput(Gate gate);
}
