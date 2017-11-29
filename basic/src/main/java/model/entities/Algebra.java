package model.entities;

import model.entities.impl.Value;

/**
 * Algebra over T6 alphabet.
 */
public interface Algebra {

    Value and(Value value);

    Value or(Value value);

    Value not();

    Fact getEasiestInput(Gate gate);

    Fact getHardestInput(Gate gate);
}
