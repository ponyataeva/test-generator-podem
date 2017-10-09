package model.entities.impl;

import model.entities.FaultValue;

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
            return Value.ONE;
        }
    },

    sa0 {
        @Override
        public Value getValue() {
            return Value.ZERO;
        }
    }
}
