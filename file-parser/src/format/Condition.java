package format;

/**
 * Add class description
 */
public class Condition {

    private Integer index;
    private String condition;

    public Condition(String condition) {
        this.condition = condition;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public String getCondition() {
        return condition;
    }

    public boolean equals(Object rule) {
        if (rule instanceof Condition) {
            return condition.equals(rule);
        } else {
            return super.equals(rule);
        }
    }
}
