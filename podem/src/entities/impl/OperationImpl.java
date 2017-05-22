package entities.impl;

import entities.Operation;
import entities.State;

/**
 * Add class description
 */
public enum OperationImpl implements Operation {

    AND {
        @Override
        public Value execute(Value value1, Value value2) {
            return value1.and(value1);
        }

        @Override
        public Value getControllingValue() {
            return Value.ZERO;
        }

        @Override
        public Value getNonControllingValue() {
            return getControllingValue().not();
        }

        @Override
        public int calculateCC0(State... vales) {
            int min = Integer.MAX_VALUE;
            for (State v : vales) {
                min = Math.min(min, v.getCC0());
            }
            return ++min;
        }

        @Override
        public int calculateCC1(State... values) {
            int sum = 0;
            for (State v : values) {
                sum = sum + v.getCC1();
            }
            return ++sum;
        }
    },

    NAND {
        @Override
        public Value execute(Value value1, Value value2) {
            return value1.and(value1).not();
        }

        @Override
        public Value getControllingValue() {
            return Value.ZERO;
        }

        @Override
        public Value getNonControllingValue() {
            return getControllingValue().not();
        }

        @Override
        public int calculateCC0(State... vales) {
            int sum = 0;
            for (State v : vales) {
                sum = sum + v.getCC1();
            }
            return sum + 1;
        }

        @Override
        public int calculateCC1(State... values) {
            int min = Integer.MAX_VALUE;
            for (State v : values) {
                min = Math.min(min, v.getCC0());
            }
            return ++min;
        }
    },

    XOR {
        @Override
        public Value execute(Value value1, Value value2) {
            return value1.not().and(value2).or(value1.and(value2.not()));
        }

        @Override
        public Value getControllingValue() {
            return null;
        }

        @Override
        public Value getNonControllingValue() {
            return null;
        }

        @Override
        public int calculateCC0(State... vales) {
            return 0;
        }

        @Override
        public int calculateCC1(State... values) {
            return 0;
        }
    }


}
