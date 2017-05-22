package entities;

import entities.impl.Value;

/**
 * Add class description
 */
public interface Operation {

    Value execute(Value value1, Value value2);

    Value getControllingValue();

    Value getNonControllingValue();

    int calculateCC0(State... values);

    int calculateCC1(State... values);
}
