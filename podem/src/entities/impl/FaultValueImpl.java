package entities.impl;

import entities.FaultValue;

import static entities.impl.Value.ONE;
import static entities.impl.Value.ZERO;

/**
 * Add class description
 */
public enum FaultValueImpl implements FaultValue {

    NONE {
        @Override
        public Value getValue() {
            return null;
        }
    },

    sa1 {
        @Override
        public Value getValue() {
            return ONE;
        }
    },

    sa0 {
        @Override
        public Value getValue() {
            return ZERO;
        }
    }
}
