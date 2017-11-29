package model.entities;

import model.entities.impl.Value;

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

    int calculateCC0(Fact... values);

    int calculateCC1(Fact... values);
}
