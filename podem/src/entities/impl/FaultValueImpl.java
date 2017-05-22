package entities.impl;

import entities.FaultValue;

import static entities.impl.Value.D;
import static entities.impl.Value.NOT_D;

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
            return D;
        }
    },

    sa0 {
        @Override
        public Value getValue() {
            return NOT_D;
        }
    }
}
