package entities;

import entities.impl.Value;

import java.util.List;

/**
 * Add class description
 */
public interface Operation {

    Value execute(List<Value> values);

    Value execute(Value value1, Value value2);

    Value executeInversion(Value value);

    Value getControllingValue();

    Value getNonControllingValue();

    int calculateCC0(State... values);

    int calculateCC1(State... values);
}
