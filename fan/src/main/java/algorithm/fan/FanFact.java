package algorithm.fan;

public class FanFact extends model.entities.Fact {

    private boolean isHeadline;

    public boolean isHeadline() {
        return isHeadline;
    }

    public FanFact setHeadline(boolean headline) {
        isHeadline = headline;
        return this;
    }
}
