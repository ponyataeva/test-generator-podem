package model.entities;

import java.util.Iterator;
import java.util.List;

/**
 * Add class description
 */
public enum Operation {

    AND {
        @Override
        public Value execute(Value value1, Value value2) {
            return value1.and(value2);
        }

        @Override
        public Value executeInversion(Value value) {
            return value;
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
        public int calculateCC0(Fact... vales) {
            int min = Integer.MAX_VALUE;
            for (Fact v : vales) {
                min = Math.min(min, v.getCC0());
            }
            return ++min;
        }

        @Override
        public int calculateCC1(Fact... values) {
            int sum = 0;
            for (Fact v : values) {
                sum = sum + v.getCC1();
            }
            return ++sum;
        }
    },

    NAND {
        @Override
        public Value execute(Value value1, Value value2) {
            return value1.and(value2);
        }

        @Override
        public Value executeInversion(Value value) {
            return value.not();
        }

        @Override
        public Value getControllingValue() {
            return Value.ONE;
        }

        @Override
        public Value getNonControllingValue() {
            return getControllingValue().not();
        }

        @Override
        public int calculateCC0(Fact... vales) {
            int sum = 0;
            for (Fact v : vales) {
                sum = sum + v.getCC1();
            }
            return sum + 1;
        }

        @Override
        public int calculateCC1(Fact... values) {
            int min = Integer.MAX_VALUE;
            for (Fact v : values) {
                min = Math.min(min, v.getCC0());
            }
            return ++min;
        }
    };

    Value execute(List<Value> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        Iterator<Value> iterator = values.iterator();
        Value result = iterator.next();

        while (iterator.hasNext()) {
            result = execute(result, iterator.next());
        }
        result = executeInversion(result);
        return result;
    }

    abstract Value execute(Value value1, Value value2);

    abstract Value executeInversion(Value value);

    abstract Value getControllingValue();

    public abstract Value getNonControllingValue();

    abstract int calculateCC0(Fact... values);

    abstract int calculateCC1(Fact... values);
}
