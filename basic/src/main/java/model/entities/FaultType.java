package model.entities;

/**
 * Add class description
 */
public enum FaultType {

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
    };

    public abstract Value getValue();
}
