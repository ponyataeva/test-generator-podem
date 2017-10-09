package model.entities.impl;

import model.entities.Algebra;
import model.entities.Gate;
import model.entities.State;

/**
 * T6 alphabet using for Podem algorithm.
 */
public enum Value implements Algebra {
    ZERO {
        @Override
        public Value and(Value value) {
            return ZERO;
        }

        @Override
        public Value or(Value value) {
            return value;
        }

        @Override
        public Value not() {
            return ONE;
        }

        @Override
        public State getEasiestInput(Gate gate) {
            return gate.getEasierPathCC0();
        }

        @Override
        public State getHardestInput(Gate gate) {
            return gate.getHardestPathCC0();
        }
    },

    ONE {
        @Override
        public Value and(Value value) {
            if (value == X) {
                return X;
            }
            return value;
        }

        @Override
        public Value or(Value value) {
            return ONE;
        }

        @Override
        public Value not() {
            return ZERO;
        }

        @Override
        public State getEasiestInput(Gate gate) {
            return gate.getEasierPathCC1();
        }

        @Override
        public State getHardestInput(Gate gate) {
            return gate.getHardestPathCC1();
        }
    },

    D {
        @Override
        public Value and(Value value) {
            if (value == ZERO || value == NOT_D) {
                return ZERO;
            } else if (value == X) {
                return X;
            }
            return D;
        }

        @Override
        public Value or(Value value) {
            if (value == ONE || value == NOT_D) {
                return ONE;
            } else if (value == X) {
                return X;
            }
            return D;
        }

        @Override
        public Value not() {
            return NOT_D;
        }

        @Override
        public State getEasiestInput(Gate gate) {
            return null;
        }

        @Override
        public State getHardestInput(Gate gate) {
            return null;
        }
    },

    NOT_D {
        @Override
        public Value and(Value value) {
            if (value == ZERO || value == D) {
                return ZERO;
            } else if (value == X) {
                return X;
            }
            return NOT_D;
        }

        @Override
        public Value or(Value value) {
            if (value == ONE || value == D) {
                return ONE;
            } else if (value == X) {
                return X;
            }
            return NOT_D;
        }

        @Override
        public Value not() {
            return D;
        }

        @Override
        public State getEasiestInput(Gate gate) {
            return null;
        }

        @Override
        public State getHardestInput(Gate gate) {
            return null;
        }
    },

    X {
        @Override
        public Value and(Value value) {
            return value == ZERO ? ZERO : X;
        }

        @Override
        public Value or(Value value) {
            return value == ONE ? ONE : X;
        }

        @Override
        public Value not() {
            return X;
        }

        @Override
        public State getEasiestInput(Gate gate) {
            return null;
        }

        @Override
        public State getHardestInput(Gate gate) {
            return null;
        }
    }
}
