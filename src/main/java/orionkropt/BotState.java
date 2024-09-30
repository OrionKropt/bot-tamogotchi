package orionkropt;

public enum BotState {
    COMMAND_EXECUTION(0),
    CHARACTER_SELECTION(1),
    DEFAULT(2);

    private final int state;

    BotState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
